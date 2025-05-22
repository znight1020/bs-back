package com.bob.domain.member.entity;

import static com.bob.support.fixture.domain.ActivityAreaFixture.defaultActivityArea;
import static com.bob.support.fixture.domain.MemberFixture.defaultIdMember;
import static com.bob.support.fixture.domain.MemberFixture.defaultMember;
import static org.assertj.core.api.Assertions.assertThat;

import com.bob.domain.area.entity.activity.ActivityArea;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("회원 도메인 테스트")
class MemberTest {

  @Test
  @DisplayName("비밀번호 수정 테스트")
  void 비밀번호를_수정할_수_있다() {
    // given
    Member member = defaultIdMember();

    // when
    member.updatePassword("newPassword");

    // then
    assertThat(member.getPassword()).isNotEqualTo(defaultMember().getPassword());
    assertThat(member.getPassword()).isEqualTo("newPassword");
  }

  @Test
  @DisplayName("활동지역 수정 테스트")
  void 활동지역을_수정할_수_있다() {
    // given
    Member member = defaultIdMember();
    ActivityArea newArea = defaultActivityArea();

    // when
    member.updateActivityArea(newArea);

    // then
    assertThat(member.getActivityArea()).isNotEqualTo(defaultMember().getActivityArea());
    assertThat(member.getActivityArea()).isEqualTo(newArea);
  }
}