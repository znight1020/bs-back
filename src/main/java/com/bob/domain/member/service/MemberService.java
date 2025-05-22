package com.bob.domain.member.service;

import static com.bob.global.exception.response.ApplicationError.ALREADY_EXISTS_EMAIL;
import static com.bob.global.exception.response.ApplicationError.INVALID_OLD_PASSWORD;
import static com.bob.global.exception.response.ApplicationError.UNVERIFIED_EMAIL;

import com.bob.domain.area.entity.EmdArea;
import com.bob.domain.area.service.reader.AreaReader;
import com.bob.domain.member.command.ChangePasswordCommand;
import com.bob.domain.member.command.CreateMemberCommand;
import com.bob.domain.member.command.IssuePasswordCommand;
import com.bob.domain.member.entity.Member;
import com.bob.domain.member.repository.MemberRepository;
import com.bob.domain.member.service.port.MailService;
import com.bob.domain.member.service.port.MailVerificationStore;
import com.bob.domain.member.service.reader.MemberReader;
import com.bob.global.exception.exceptions.ApplicationException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional
@Service
public class MemberService {

  private final MemberRepository memberRepository;
  private final AreaReader areaReader;
  private final PasswordEncoder encoder;
  private final MailVerificationStore mailVerificationStore;
  private final MailService mailService;
  private final MemberReader memberReader;

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
