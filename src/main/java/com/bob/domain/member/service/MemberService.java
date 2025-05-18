package com.bob.domain.member.service;

import static com.bob.global.exception.response.ApplicationError.ALREADY_EXISTS_EMAIL;
import static com.bob.global.exception.response.ApplicationError.UNVERIFIED_EMAIL;

import com.bob.domain.area.entity.EmdArea;
import com.bob.domain.area.service.reader.AreaReader;
import com.bob.domain.member.command.CreateMemberCommand;
import com.bob.domain.member.entity.Member;
import com.bob.domain.member.repository.MemberRepository;
import com.bob.domain.member.service.port.MailVerificationStore;
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
}
