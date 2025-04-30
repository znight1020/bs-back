package toy.bookswap.domain.member.service;

import static toy.bookswap.global.exception.response.ApplicationError.EMAIL_ALREADY_EXISTS;
import static toy.bookswap.global.exception.response.ApplicationError.EMAIL_IS_NOT_VERIFIED;

import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import toy.bookswap.domain.member.command.CreateMemberCommand;
import toy.bookswap.domain.member.entity.Member;
import toy.bookswap.domain.member.repository.MemberRepository;
import toy.bookswap.global.exception.exceptions.ApplicationException;

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
      throw new ApplicationException(EMAIL_ALREADY_EXISTS);
    }
  }

  private void verifyEmail(String email) {
    if (!Objects.equals(redisTemplate.opsForValue().get("email-verified:" + email), "true")) {
      throw new ApplicationException(EMAIL_IS_NOT_VERIFIED);
    }
    redisTemplate.delete("email-verified:" + email);
  }
}
