package toy.bookswap.domain.member.service;

import lombok.RequiredArgsConstructor;
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

  public void signupProcess(CreateMemberCommand command) {
    Member newMember = Member.builder()
        .email(command.email())
        .password(encoder.encode(command.password()))
        .nickname(command.nickname())
        .build();
    memberRepository.save(newMember);
  }
}
