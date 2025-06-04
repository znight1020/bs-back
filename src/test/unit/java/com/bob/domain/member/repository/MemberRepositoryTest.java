package com.bob.domain.member.repository;

import static com.bob.support.fixture.domain.MemberFixture.defaultMember;
import static org.assertj.core.api.Assertions.assertThat;

import com.bob.domain.member.entity.Member;
import com.bob.support.config.TestConfig;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

@Import(TestConfig.class)
@DataJpaTest
@DisplayName("MemberRepository 테스트")
class MemberRepositoryTest {

  @Autowired
  private MemberRepository memberRepository;

  @Test
  @DisplayName("이메일을 통한 회원 조회 - 성공 테스트")
  void 이메일로_회원정보를_조회할_수_있다() {
    // given
    Member member = defaultMember();
    memberRepository.save(member);
    String email = member.getEmail();

    // when
    Optional<Member> result = memberRepository.findByEmail(email);

    // then
    assertThat(result).isPresent();
    assertThat(result.get().getEmail()).isEqualTo(email);
    assertThat(result.get().getPassword()).isEqualTo(member.getPassword());
    assertThat(result.get().getNickname()).isEqualTo(member.getNickname());
  }

  @Test
  @DisplayName("이메일을 통한 회원 조회 - 실패 테스트 (이메일 존재 X)")
  void 이메일로_회원을_찾지_못하면_빈_Optional을_반환한다() {
    // when
    Optional<Member> result = memberRepository.findByEmail("notfound@email.com");

    // then
    assertThat(result).isEmpty();
  }
}
