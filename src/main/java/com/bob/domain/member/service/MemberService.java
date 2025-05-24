package com.bob.domain.member.service;

import static com.bob.global.exception.response.ApplicationError.ALREADY_EXISTS_EMAIL;
import static com.bob.global.exception.response.ApplicationError.INVALID_OLD_PASSWORD;
import static com.bob.global.exception.response.ApplicationError.UNVERIFIED_EMAIL;

import com.bob.domain.area.entity.EmdArea;
import com.bob.domain.area.service.reader.AreaReader;
import com.bob.domain.member.service.dto.command.ChangePasswordCommand;
import com.bob.domain.member.service.dto.command.CreateMemberCommand;
import com.bob.domain.member.service.dto.command.IssuePasswordCommand;
import com.bob.domain.member.service.dto.query.ReadProfileQuery;
import com.bob.domain.member.service.dto.query.ReadProfileWithPostsQuery;
import com.bob.domain.member.service.dto.response.internal.MemberPost;
import com.bob.domain.member.service.dto.response.MemberProfileResponse;
import com.bob.domain.member.service.dto.response.MemberProfileWithPostsResponse;
import com.bob.domain.member.entity.Member;
import com.bob.domain.member.repository.MemberRepository;
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
@Transactional
@Service
public class MemberService {

  private final MemberRepository memberRepository;
  private final MemberReader memberReader;
  private final AreaReader areaReader;
  private final MailService mailService;
  private final MailVerificationStore mailVerificationStore;
  private final PostSearcher postSearcher;
  private final PasswordEncoder encoder;

  public void signupProcess(CreateMemberCommand command) {
    verifyAlreadyExistEmail(command.email());
    verifyEmail(command.email());

    EmdArea memberEmdArea = areaReader.readEmdArea(command.emdId());
    String encodedPassword = encoder.encode(command.password());

    Member member = command.toMember(memberEmdArea, encodedPassword);
    memberRepository.save(member);
  }

  private void verifyAlreadyExistEmail(String email) {
    if (memberRepository.existsByEmail(email)) {
      throw new ApplicationException(ALREADY_EXISTS_EMAIL);
    }
  }

  private void verifyEmail(String email) {
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

  public void issueTempPasswordProcess(IssuePasswordCommand command) {
    Member member = memberReader.readMemberByEmail(command.email());
    String tempPassword = mailService.sendTempPasswordProcess(command.email());
    member.updatePassword(encoder.encode(tempPassword));
  }
}
