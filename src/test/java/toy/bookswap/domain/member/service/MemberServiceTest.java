package toy.bookswap.domain.member.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import toy.bookswap.domain.member.command.CreateMemberCommand;
import toy.bookswap.domain.member.entity.Member;
import toy.bookswap.domain.member.repository.MemberRepository;

@DisplayName("사용자 회원가입 테스트")
@ExtendWith(MockitoExtension.class)
class MemberSingupServiceTest {

  @Mock
  private MemberRepository memberRepository;

  @Mock
  private BCryptPasswordEncoder encoder;

  @Mock
  private StringRedisTemplate redisTemplate;

  @Mock
  private ValueOperations<String, String> valueOperations;

  @InjectMocks
  private MemberService memberService;

  @Test
  @DisplayName("회원가입 - 성공 테스트")
  void signupProcessTest() {
    // given
    String rawPassword = "password";
    String encodedPassword = "$2a$10";
    String email = "test@email.com";
    CreateMemberCommand command = new CreateMemberCommand(email, rawPassword, "tester");

    ArgumentCaptor<Member> captor = ArgumentCaptor.forClass(Member.class);

    given(encoder.encode(rawPassword)).willReturn(encodedPassword);
    given(redisTemplate.opsForValue()).willReturn(valueOperations);
    given(valueOperations.get("email-verified:" + email)).willReturn("true");

    // when
    memberService.signupProcess(command);

    // then
    then(valueOperations).should().get("email-verified:" + email);
    then(memberRepository).should(times(1)).save(captor.capture());
    Member saved = captor.getValue();
    assertThat(saved.getEmail()).isEqualTo("test@email.com");
    assertThat(saved.getNickname()).isEqualTo("tester");
    assertThat(saved.getPassword()).isEqualTo(encodedPassword);
    then(redisTemplate).should().delete("email-verified:" + email);
  }

  @Test
  @DisplayName("회원가입 - 실패 테스트(이메일 인증 X)")
  void signupProcessFailNotVerifiedTest() {
    // given
    String email = "test@email.com";
    CreateMemberCommand command = new CreateMemberCommand(email, "password", "tester");

    given(redisTemplate.opsForValue()).willReturn(valueOperations);
    given(valueOperations.get("email-verified:" + email)).willReturn(null);

    // when & then
    assertThatThrownBy(() -> memberService.signupProcess(command))
        .isInstanceOf(RuntimeException.class)
        .hasMessage("이메일 인증이 완료되지 않았습니다.");
  }

}