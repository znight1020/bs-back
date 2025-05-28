package com.bob.domain.member.service;

import static com.bob.global.exception.response.ApplicationError.ALREADY_EXISTS_EMAIL;
import static com.bob.global.exception.response.ApplicationError.INVALID_OLD_PASSWORD;
import static com.bob.global.exception.response.ApplicationError.IS_SAME_REQUEST;
import static com.bob.global.exception.response.ApplicationError.UNVERIFIED_EMAIL;
import static com.bob.global.utils.image.ImageDirectory.PROFILE;
import static com.bob.global.utils.image.ImageUtils.generateImageFileName;

import com.bob.domain.area.entity.EmdArea;
import com.bob.domain.area.service.reader.AreaReader;
import com.bob.domain.member.entity.Member;
import com.bob.domain.member.repository.MemberRepository;
import com.bob.domain.member.service.dto.command.ChangePasswordCommand;
import com.bob.domain.member.service.dto.command.ChangeProfileCommand;
import com.bob.domain.member.service.dto.command.CreateMemberCommand;
import com.bob.domain.member.service.dto.command.IssuePasswordCommand;
import com.bob.domain.member.service.dto.command.ChangeProfileImageUrlCommand;
import com.bob.domain.member.service.dto.query.ReadProfileQuery;
import com.bob.domain.member.service.dto.query.ReadProfileWithPostsQuery;
import com.bob.domain.member.service.dto.response.MemberProfileImageUrlResponse;
import com.bob.domain.member.service.dto.response.MemberProfileResponse;
import com.bob.domain.member.service.dto.response.MemberProfileWithPostsResponse;
import com.bob.domain.member.service.dto.response.internal.MemberPost;
import com.bob.domain.member.service.port.ImageStorageAccessor;
import com.bob.domain.member.service.port.MailService;
import com.bob.domain.member.service.port.MailVerificationStore;
import com.bob.domain.member.service.port.PostSearcher;
import com.bob.domain.member.service.reader.MemberReader;
import com.bob.global.exception.exceptions.ApplicationException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class MemberService {

  private final MemberRepository memberRepository;
  private final MemberReader memberReader;
  private final AreaReader areaReader;

  private final ImageStorageAccessor imageStorageAccessor;
  private final MailVerificationStore mailVerificationStore;
  private final MailService mailService;
  private final PostSearcher postSearcher;

  private final PasswordEncoder encoder;

  @Transactional
  public void signupProcess(CreateMemberCommand command) {
    verifyEmail(command.email());

    EmdArea memberEmdArea = areaReader.readEmdArea(command.emdId());
    String encodedPassword = encoder.encode(command.password());

    Member member = command.toMember(memberEmdArea, encodedPassword);
    memberRepository.save(member);
  }

  private void verifyEmail(String email) {
    if (memberRepository.existsByEmail(email)) {
      throw new ApplicationException(ALREADY_EXISTS_EMAIL);
    }

    boolean isVerified = mailVerificationStore.getVerified(email)
        .map("true"::equals)
        .orElse(false);

    if (!isVerified) {
      throw new ApplicationException(UNVERIFIED_EMAIL);
    }

    mailVerificationStore.deleteVerified(email);
  }

  @Transactional(readOnly = true)
  public MemberProfileResponse readProfileProcess(ReadProfileQuery query) {
    Member member = memberReader.readMemberById(query.memberId());
    return MemberProfileResponse.of(member);
  }

  @Transactional(readOnly = true)
  public MemberProfileWithPostsResponse readProfileByIdWithPostsProcess(ReadProfileWithPostsQuery query) {
    Member member = memberReader.readMemberById(query.memberId());
    MemberProfileResponse profile = MemberProfileResponse.of(member);
    List<MemberPost> posts = MemberPost.from(postSearcher.readPostsOfMember(query.memberId(), query.pageable()));
    return MemberProfileWithPostsResponse.of(profile, posts);
  }

  @Transactional
  public void changeProfileProcess(ChangeProfileCommand command) {
    Member member = memberReader.readMemberById(command.memberId());
    verifyNickname(member, command.nickname());
    member.updateNickname(command.nickname());
  }

  private static void verifyNickname(Member member, String nickname) {
    if (member.isEqualsNickname(nickname)) {
      throw new ApplicationException(IS_SAME_REQUEST);
    }
  }

  @Transactional
  public void changePasswordProcess(ChangePasswordCommand command) {
    Member member = memberReader.readMemberById(command.memberId());
    verifyPassword(member.getPassword(), command.oldPassword());
    member.updatePassword(encoder.encode(command.newPassword()));
  }

  private void verifyPassword(String memberPassword, String receiveOldPassword) {
    if (!encoder.matches(receiveOldPassword, memberPassword)) {
      throw new ApplicationException(INVALID_OLD_PASSWORD);
    }
  }

  @Transactional
  public void issueTempPasswordProcess(IssuePasswordCommand command) {
    Member member = memberReader.readMemberByEmail(command.email());
    String tempPassword = mailService.sendTempPasswordProcess(command.email());
    member.updatePassword(encoder.encode(tempPassword));
  }

  @Transactional
  public MemberProfileImageUrlResponse changeProfileImageUrlProcess(ChangeProfileImageUrlCommand command) {
    String fileName = generateImageFileName(command.contentType(), PROFILE);
    String signedUrl = imageStorageAccessor.getImageUploadUrl(fileName, command.contentType());
    Member member = memberReader.readMemberById(command.memberId());
    member.updateProfileImageUrl(fileName);
    return MemberProfileImageUrlResponse.of(fileName, signedUrl);
  }
}
