package toy.bookswap.domain.member.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import toy.bookswap.domain.member.command.CreateMemberCommand;
import toy.bookswap.domain.member.entity.Member;
import toy.bookswap.domain.member.repository.MemberRepository;

@ExtendWith(MockitoExtension.class)
class MemberServiceTest {

  @Mock
  private MemberRepository memberRepository;

  @Mock
  private BCryptPasswordEncoder encoder;

  @InjectMocks
  private MemberService memberService;

  @Test
  void 회원가입_시_비밀번호는_암호화되어_저장된다() {
    // given
    String rawPassword = "password";
    String encodedPassword = "$2a$10";
    CreateMemberCommand command = new CreateMemberCommand("test@email.com", rawPassword, "tester");

    ArgumentCaptor<Member> captor = ArgumentCaptor.forClass(Member.class);
    given(encoder.encode(rawPassword)).willReturn(encodedPassword);

    // when
    memberService.signupProcess(command);

    // then
    then(memberRepository).should(times(1)).save(captor.capture());

    Member saved = captor.getValue();
    assertThat(saved.getEmail()).isEqualTo("test@email.com");
    assertThat(saved.getNickname()).isEqualTo("tester");
    assertThat(saved.getPassword()).isEqualTo(encodedPassword);
  }
}