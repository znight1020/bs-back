package toy.bookswap.domain.member.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import toy.bookswap.domain.member.entity.Member;

@DataJpaTest
@DisplayName("MemberRepository 테스트")
class MemberRepositoryTest {

  @Autowired
  private MemberRepository memberRepository;

  @Test
  @DisplayName("이메일로 회원을 조회할 수 있다")
  void 이메일로_회원정보를_조회할_수_있다() {
    // given
    Member member = Member.builder()
        .email("test@email.com")
        .password("encrypted-password")
        .nickname("테스터")
        .build();

    memberRepository.save(member);

    // when
    Optional<Member> result = memberRepository.findByEmail("test@email.com");

    // then
    assertThat(result).isPresent();
    assertThat(result.get().getEmail()).isEqualTo("test@email.com");
    assertThat(result.get().getPassword()).isEqualTo("encrypted-password");
    assertThat(result.get().getNickname()).isEqualTo("테스터");
  }

  @Test
  @DisplayName("이메일로 회원을 찾지 못하면 빈 Optional을 반환한다")
  void 이메일로_회원을_찾지_못하면_빈_Optional을_반환한다() {
    // when
    Optional<Member> result = memberRepository.findByEmail("notfound@email.com");

    // then
    assertThat(result).isEmpty();
  }
}
