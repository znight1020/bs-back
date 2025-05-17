package com.bob.domain.member.service;

import static com.bob.global.exception.response.ApplicationError.ALREADY_EXISTS_EMAIL;
import static com.bob.global.exception.response.ApplicationError.UNVERIFIED_EMAIL;

import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.bob.domain.member.command.CreateMemberCommand;
import com.bob.domain.member.entity.Member;
import com.bob.domain.member.repository.MemberRepository;
import com.bob.global.exception.exceptions.ApplicationException;

@RequiredArgsConstructor
@Transactional
@Service
public class MemberService {

  private final PasswordEncoder encoder;
  private final MemberRepository memberRepository;
  private final StringRedisTemplate redisTemplate;

  public void signupProcess(CreateMemberCommand command) {
    verifyAlreadyExistEmail(command.email());
    verifyEmail(command.email());
    Member newMember = Member.builder()
        .email(command.email())
        .password(encoder.encode(command.password()))
        .nickname(command.nickname())
        .build();
    memberRepository.save(newMember);
  }

  private void verifyAlreadyExistEmail(String email) {
    if (memberRepository.existsByEmail(email)) {
      throw new ApplicationException(ALREADY_EXISTS_EMAIL);
    }
  }

  private void verifyEmail(String email) {
    if (!Objects.equals(redisTemplate.opsForValue().get("email-verified:" + email), "true")) {
      throw new ApplicationException(UNVERIFIED_EMAIL);
    }
    redisTemplate.delete("email-verified:" + email);
  }
}
