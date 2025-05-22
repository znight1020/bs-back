package com.bob.domain.area.service;

import static com.bob.global.exception.response.ApplicationError.INVALID_AREA_AUTHENTICATION;
import static com.bob.global.exception.response.ApplicationError.NOT_EXISTS_MEMBER;
import static com.bob.support.fixture.command.AuthenticationCommandFixture.defaultChangeAreaCommand;
import static com.bob.support.fixture.command.AuthenticationCommandFixture.defaultReAuthenticateCommand;
import static com.bob.support.fixture.command.AuthenticationCommandFixture.defaultSignUpCommand;
import static com.bob.support.fixture.command.AuthenticationCommandFixture.guestCommand;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.any;
import static org.mockito.BDDMockito.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.mock;
import static org.mockito.BDDMockito.then;

import com.bob.domain.area.command.AuthenticationCommand;
import com.bob.domain.area.entity.EmdArea;
import com.bob.domain.area.entity.activity.ActivityArea;
import com.bob.domain.area.entity.activity.ActivityAreaId;
import com.bob.domain.area.repository.ActivityAreaRepository;
import com.bob.domain.area.service.reader.ActivityAreaReader;
import com.bob.domain.area.service.reader.AreaReader;
import com.bob.domain.member.command.AuthenticationPurpose;
import com.bob.domain.member.entity.Member;
import com.bob.domain.member.service.reader.MemberReader;
import com.bob.global.exception.exceptions.ApplicationException;
import java.time.LocalDate;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.Point;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@DisplayName("위치 인증 서비스 단위 테스트")
@ExtendWith(MockitoExtension.class)
class AreaServiceTest {

  @InjectMocks
  private AreaService areaService;

  @Mock
  private ActivityAreaRepository activityAreaRepository;

  @Mock
  private ActivityAreaReader activityAreaReader;

  @Mock
  private AreaReader areaReader;

  @Mock
  private MemberReader memberReader;

  @Mock
  private EmdArea emdArea;

  @Mock
  private Geometry geometry;

  @Test
  @DisplayName("회원가입 시 행정구역 안에 있는 경우 - 성공 테스트")
  void 회원가입_좌표_인증_성공() {
    // given
    AuthenticationCommand command = defaultSignUpCommand();
    given(areaReader.readEmdArea(command.emdId())).willReturn(emdArea);
    given(emdArea.getGeom()).willReturn(geometry);
    given(geometry.contains(any(Point.class))).willReturn(true);

    // when & then
    areaService.authenticate(command); // 예외 없으면 성공
  }

  @Test
  @DisplayName("회원가입 시 행정구역 밖에 있는 경우 - 실패 테스트")
  void 행정구역_밖이면_예외_발생() {
    // given
    AuthenticationCommand command = defaultSignUpCommand();
    given(areaReader.readEmdArea(command.emdId())).willReturn(emdArea);
    given(emdArea.getGeom()).willReturn(geometry);
    given(geometry.contains(any(Point.class))).willReturn(false);

    // when & then
    assertThatThrownBy(() -> areaService.authenticate(command))
        .isInstanceOf(ApplicationException.class)
        .hasMessage(INVALID_AREA_AUTHENTICATION.getMessage());
  }

  @Test
  @DisplayName("활동지역 변경 - 성공 테스트")
  void 활동지역_변경_성공() {
    // given
    AuthenticationCommand command = defaultChangeAreaCommand();
    given(areaReader.readEmdArea(command.emdId())).willReturn(emdArea);
    given(emdArea.getGeom()).willReturn(geometry);
    given(geometry.contains(any(Point.class))).willReturn(true);

    Member member = mock(Member.class);
    given(memberReader.readMemberById(command.memberId())).willReturn(member);

    // when
    areaService.authenticate(command);

    // then
    then(activityAreaRepository).should().deleteById(ActivityArea.createId(command.memberId(), command.emdId()));
    then(activityAreaRepository).should().save(any(ActivityArea.class));
    then(member).should().updateActivityArea(any(ActivityArea.class));
  }

  @Test
  @DisplayName("활동지역 변경 시 회원이 아닌 경우 - 실패 테스트")
  void 활동지역_변경_게스트_예외() {
    // given
    AuthenticationCommand command = guestCommand(AuthenticationPurpose.CHANGE_AREA);
    given(areaReader.readEmdArea(command.emdId())).willReturn(emdArea);
    given(emdArea.getGeom()).willReturn(geometry);
    given(geometry.contains(any(Point.class))).willReturn(true);

    // when & then
    assertThatThrownBy(() -> areaService.authenticate(command))
        .isInstanceOf(ApplicationException.class)
        .hasMessage(NOT_EXISTS_MEMBER.getMessage());
  }

  @Test
  @DisplayName("활동지역 재인증 - 성공 테스트")
  void 활동지역_재인증_성공() {
    // given
    AuthenticationCommand command = defaultReAuthenticateCommand();
    given(areaReader.readEmdArea(command.emdId())).willReturn(emdArea);
    given(emdArea.getGeom()).willReturn(geometry);
    given(geometry.contains(any(Point.class))).willReturn(true);

    ActivityArea activityArea = mock(ActivityArea.class);
    given(activityAreaReader.readActivityArea(any(ActivityAreaId.class))).willReturn(activityArea);

    // when
    areaService.authenticate(command);

    // then
    then(activityArea).should().updateAuthenticationAt(eq(LocalDate.now()));
  }

  @Test
  @DisplayName("활동지역 재인증 시 회원이 아닌 경우 - 실패 테스트")
  void 활동지역_재인증_게스트_예외() {
    // given
    AuthenticationCommand command = guestCommand(AuthenticationPurpose.RE_AUTHENTICATE);
    EmdArea emdArea = mock(EmdArea.class);
    Geometry geometry = mock(Geometry.class);

    given(areaReader.readEmdArea(command.emdId())).willReturn(emdArea);
    given(emdArea.getGeom()).willReturn(geometry);
    given(geometry.contains(any(Point.class))).willReturn(true);

    // when & then
    assertThatThrownBy(() -> areaService.authenticate(command))
        .isInstanceOf(ApplicationException.class)
        .hasMessage(NOT_EXISTS_MEMBER.getMessage());
  }

}
