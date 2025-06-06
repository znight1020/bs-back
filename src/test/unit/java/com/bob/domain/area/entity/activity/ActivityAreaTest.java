package com.bob.domain.area.entity.activity;

import static com.bob.support.fixture.domain.ActivityAreaFixture.customTimeActivityArea;
import static com.bob.support.fixture.domain.EmdAreaFixture.defaultEmdArea;
import static com.bob.support.fixture.domain.MemberFixture.MEMBER_ID;
import static com.bob.support.fixture.domain.MemberFixture.defaultIdMember;
import static org.assertj.core.api.Assertions.assertThat;

import com.bob.domain.area.entity.EmdArea;
import com.bob.domain.member.entity.Member;
import java.time.LocalDate;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("활동지역 도메인 테스트")
class ActivityAreaTest {

  @Test
  @DisplayName("ActivityArea 식별자 생성 테스트")
  void ActivityAreaId를_정상적으로_생성할_수_있다() {
    // given
    UUID memberId = MEMBER_ID;
    Integer emdAreaId = 213;

    // when
    ActivityAreaId id = ActivityArea.createId(memberId, emdAreaId);

    // then
    assertThat(id.getMemberId()).isEqualTo(memberId);
    assertThat(id.getEmdAreaId()).isEqualTo(emdAreaId);
  }

  @Test
  @DisplayName("ActivityArea 생성 테스트")
  void ActivityArea를_정상적으로_생성할_수_있다() {
    // given
    Member member = defaultIdMember();
    EmdArea emdArea = defaultEmdArea();
    ActivityAreaId id = ActivityArea.createId(member.getId(), emdArea.getId());

    // when
    ActivityArea activityArea = ActivityArea.create(id, member, emdArea);

    // then
    assertThat(activityArea.getId()).isEqualTo(id);
    assertThat(activityArea.getMember()).isEqualTo(member);
    assertThat(activityArea.getEmdArea()).isEqualTo(emdArea);
    assertThat(activityArea.getAuthenticationAt()).isEqualTo(LocalDate.now());
  }

  @Test
  @DisplayName("인증 날짜 갱신 테스트")
  void 인증날짜를_정상적으로_갱신할_수_있다() {
    // given
    Member member = defaultIdMember();
    EmdArea emdArea = defaultEmdArea();
    ActivityAreaId id = ActivityArea.createId(member.getId(), emdArea.getId());
    ActivityArea activityArea = ActivityArea.create(id, member, emdArea);

    // when
    LocalDate newDate = LocalDate.of(2025, 5, 1);
    activityArea.updateAuthenticationAt(newDate);

    // then
    assertThat(activityArea.getAuthenticationAt()).isEqualTo(newDate);
  }

  @Test
  @DisplayName("활동지역 인증 - 유효")
  void 인증이_유효하면_true를_반환한다() {
    // given
    Member member = defaultIdMember();
    EmdArea emdArea = defaultEmdArea();
    ActivityArea activityArea = customTimeActivityArea(member, emdArea, LocalDate.now().minusWeeks(1)); // 1주일 전

    // when
    boolean isValid = activityArea.isValidAuthentication();

    // then
    assertThat(isValid).isTrue();
  }

  @Test
  @DisplayName("활동지역 인증 - 만료")
  void 인증이_만료되면_false를_반환한다() {
    // given
    Member member = defaultIdMember();
    EmdArea emdArea = defaultEmdArea();
    ActivityArea activityArea = customTimeActivityArea(member, emdArea, LocalDate.now().minusMonths(2)); // 2달 전

    // when
    boolean isValid = activityArea.isValidAuthentication();

    // then
    assertThat(isValid).isFalse();
  }
}