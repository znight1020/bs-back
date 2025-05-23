package com.bob.domain.area.service;

import static com.bob.domain.member.dto.command.AuthenticationPurpose.CHANGE_AREA;
import static com.bob.domain.member.dto.command.AuthenticationPurpose.RE_AUTHENTICATE;
import static com.bob.global.exception.response.ApplicationError.INVALID_AREA_AUTHENTICATION;
import static com.bob.global.exception.response.ApplicationError.NOT_EXISTS_MEMBER;
import static com.bob.support.fixture.command.AuthenticationCommandFixture.customAuthenticationCommand;
import static com.bob.support.fixture.command.AuthenticationCommandFixture.defaultAuthenticationCommand;
import static com.bob.support.fixture.command.AuthenticationCommandFixture.defaultMismatchAuthenticationCommand;
import static com.bob.support.fixture.command.AuthenticationCommandFixture.guestCommand;
import static com.bob.support.fixture.domain.ActivityAreaFixture.customActivityArea;
import static com.bob.support.fixture.domain.EmdAreaFixture.defaultEmdArea;
import static com.bob.support.fixture.domain.MemberFixture.defaultMember;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.bob.domain.area.command.AuthenticationCommand;
import com.bob.domain.area.entity.EmdArea;
import com.bob.domain.area.entity.activity.ActivityArea;
import com.bob.domain.area.entity.activity.ActivityAreaId;
import com.bob.domain.area.repository.ActivityAreaRepository;
import com.bob.domain.area.repository.AreaRepository;
import com.bob.domain.member.dto.command.AuthenticationPurpose;
import com.bob.domain.member.entity.Member;
import com.bob.domain.member.repository.MemberRepository;
import com.bob.global.exception.exceptions.ApplicationException;
import com.bob.support.TestContainerSupport;
import com.bob.support.redis.RedisContainerConfig;
import java.time.LocalDate;
import java.util.stream.Stream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.annotation.Transactional;

@DisplayName("위치 서비스 통합 테스트")
@Import(RedisContainerConfig.class)
@Transactional
@SpringBootTest
class AreaServiceIntgTest extends TestContainerSupport {

  @Autowired
  private AreaService areaService;

  @Autowired
  private AreaRepository areaRepository;

  @Autowired
  private MemberRepository memberRepository;

  @Autowired
  private ActivityAreaRepository activityAreaRepository;

  private EmdArea emdArea;

  private Member member;

  @BeforeEach
  void setUp() {
    emdArea = areaRepository.findById(defaultEmdArea().getId()).get();
    member = memberRepository.save(defaultMember());
  }

  @Test
  @DisplayName("사용자 위치가 활동지역과 일치 - 성공 테스트")
  void 회원가입_성공() {
    // given
    AuthenticationCommand command = defaultAuthenticationCommand();

    // when & then
    areaService.authenticate(command);
  }

  @Test
  @DisplayName("사용자 위치가 활동지역과 불일치 - 실패 테스트")
  void 회원가입_실패() {
    // given
    AuthenticationCommand command = defaultMismatchAuthenticationCommand();

    // when & then
    assertThatThrownBy(() -> areaService.authenticate(command))
        .isInstanceOf(ApplicationException.class)
        .hasMessage(INVALID_AREA_AUTHENTICATION.getMessage());
  }

  @Test
  @DisplayName("활동지역 변경 - 성공 테스트")
  void 활동지역_변경() {
    // given
    AuthenticationCommand command = customAuthenticationCommand(member.getId(), CHANGE_AREA);

    // when
    System.out.println("memberID: " + member.getId());
    System.out.println("command.memberID: " + command.memberId());
    areaService.authenticate(command);

    // then
    ActivityAreaId id = new ActivityAreaId(member.getId(), emdArea.getId());
    ActivityArea saved = activityAreaRepository.findById(id).orElseThrow();

    assertThat(saved.getMember().getId()).isEqualTo(member.getId());
    assertThat(saved.getEmdArea().getId()).isEqualTo(emdArea.getId());
  }

  @Test
  @DisplayName("활동지역 재인증 - 성공 테스트")
  void 재인증_성공() {
    // given
    ActivityArea area = customActivityArea(member, emdArea);
    activityAreaRepository.save(area);
    AuthenticationCommand command = customAuthenticationCommand(member.getId(), RE_AUTHENTICATE);

    // when
    areaService.authenticate(command);

    // then
    ActivityAreaId id = new ActivityAreaId(member.getId(), emdArea.getId());
    ActivityArea updated = activityAreaRepository.findById(id).orElseThrow();
    assertThat(updated.getAuthenticationAt()).isEqualTo(LocalDate.now());
  }

  @ParameterizedTest
  @MethodSource("요청_목록") // CHANGE_AREA: 위치 변경, RE_AUTHENTICATE: 위치 재인증
  @DisplayName("비회원 인증 요청 - 실패 테스트")
  void 게스트_예외(AuthenticationPurpose purpose) {
    // given
    AuthenticationCommand command = guestCommand(purpose);

    // when & then
    assertThatThrownBy(() -> areaService.authenticate(command))
        .isInstanceOf(ApplicationException.class)
        .hasMessage(NOT_EXISTS_MEMBER.getMessage());
  }

  private static Stream<AuthenticationPurpose> 요청_목록() {
    return Stream.of(CHANGE_AREA, RE_AUTHENTICATE);
  }
}
