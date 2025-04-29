package toy.bookswap.global.auth.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;

import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import toy.bookswap.domain.member.entity.Member;
import toy.bookswap.domain.member.repository.MemberRepository;
import toy.bookswap.global.auth.response.MemberDetails;

@DisplayName("MemberDetailsService 테스트")
@ExtendWith(MockitoExtension.class)
class MemberDetailsServiceTest {

  @InjectMocks
  private MemberDetailsService memberDetailsService;

  @Mock
  private MemberRepository memberRepository;

  private Member member;

  @BeforeEach
  void setUp() {
    member = Member.builder()
        .id(1L)
        .email("test@email.com")
        .password("encrypted-password")
        .build();
  }

  @Test
  @DisplayName("회원 조회 - 성공 테스트")
  void 이메일로_회원정보를_조회할_수_있다() {
    // given
    given(memberRepository.findByEmail("test@email.com")).willReturn(Optional.of(member));

    // when
    MemberDetails userDetails = (MemberDetails) memberDetailsService.loadUserByUsername("test@email.com");

    // then
    assertThat(userDetails).isNotNull();
    assertThat(userDetails.id()).isEqualTo(1L);
    assertThat(userDetails.email()).isEqualTo("test@email.com");
    assertThat(userDetails.password()).isEqualTo("encrypted-password");
  }

  @Test
  @DisplayName("회원 조회 - 실패 테스트(이메일 존재 X)")
  void 이메일로_회원을_찾지_못하면_예외를_던진다() {
    // given
    given(memberRepository.findByEmail("unknown@email.com")).willReturn(Optional.empty());

    // when & then
    assertThatThrownBy(() -> memberDetailsService.loadUserByUsername("unknown@email.com"))
        .isInstanceOf(UsernameNotFoundException.class)
        .hasMessage("unknown@email.com");
  }
}
