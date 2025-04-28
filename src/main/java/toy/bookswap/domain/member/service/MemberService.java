package toy.bookswap.domain.member.service;

import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import toy.bookswap.domain.member.command.CreateMemberCommand;
import toy.bookswap.domain.member.entity.Member;
import toy.bookswap.domain.member.repository.MemberRepository;

@RequiredArgsConstructor
@Transactional
@Service
public class MemberService {

  private final BCryptPasswordEncoder encoder;
  private final MemberRepository memberRepository;
  private final StringRedisTemplate redisTemplate;

  public void signupProcess(CreateMemberCommand command) {
    verifyEmail(command.email());
    Member newMember = Member.builder()
        .email(command.email())
        .password(encoder.encode(command.password()))
        .nickname(command.nickname())
        .build();
    memberRepository.save(newMember);
  }

  private void verifyEmail(String email) {
    // TODO - 공통 Exception 처리
    if(!Objects.equals(redisTemplate.opsForValue().get("email-verified:" + email), "true")) {
      throw new RuntimeException("이메일 인증이 완료되지 않았습니다.");
    }
    redisTemplate.delete("email-verified:" + email);
  }
}
