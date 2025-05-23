package com.bob.domain.member.service;

import static com.bob.support.fixture.command.MemberCommandFixture.customChangePasswordCommand;
import static com.bob.support.fixture.command.MemberCommandFixture.defaultCreateMemberCommand;
import static com.bob.support.fixture.command.MemberCommandFixture.defaultIssuePasswordCommand;
import static com.bob.support.fixture.domain.ActivityAreaFixture.defaultActivityArea;
import static com.bob.support.fixture.domain.MemberFixture.customEmailMember;
import static com.bob.support.fixture.domain.MemberFixture.defaultMember;
import static com.bob.support.fixture.domain.MemberFixture.encryptPasswordMember;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;

import com.bob.domain.member.dto.command.ChangePasswordCommand;
import com.bob.domain.member.dto.command.CreateMemberCommand;
import com.bob.domain.member.dto.command.IssuePasswordCommand;
import com.bob.domain.member.dto.query.ReadProfileQuery;
import com.bob.domain.member.dto.response.MemberProfileResponse;
import com.bob.domain.member.entity.Member;
import com.bob.domain.member.repository.MemberRepository;
import com.bob.domain.member.service.port.MailService;
import com.bob.domain.member.service.port.MailVerificationStore;
import com.bob.global.exception.exceptions.ApplicationException;
import com.bob.global.exception.response.ApplicationError;
import com.bob.support.TestContainerSupport;
import com.bob.support.redis.RedisContainerConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.transaction.annotation.Transactional;

@DisplayName("사용자 서비스 통합 테스트")
@Import(RedisContainerConfig.class)
@Transactional
@SpringBootTest
class MemberServiceIntgTest extends TestContainerSupport {

  @Autowired
  private MemberService memberService;

  @Autowired
  private MemberRepository memberRepository;

  @Autowired
  private PasswordEncoder passwordEncoder;

  @Autowired
  private MailVerificationStore mailVerificationStore;

  @MockitoBean
  private MailService mailService;

  @BeforeEach
  void init() {
    memberRepository.deleteAll();
  }

  @Test
  @DisplayName("회원가입 - 성공 테스트")
  void 이메일_인증이_완료된_사용자는_회원가입을_할_수_있다() {
    // given
    String email = "test@email.com";
    mailVerificationStore.saveVerified(email, "true", 5);
    CreateMemberCommand command = defaultCreateMemberCommand();

    // when
    memberService.signupProcess(command);

    // then
    Member saved = memberRepository.findByEmail(email).orElseThrow();
    assertThat(saved.getEmail()).isEqualTo(email);
    assertThat(passwordEncoder.matches(command.password(), saved.getPassword())).isTrue();
    assertThat(saved.getNickname()).isEqualTo(command.nickname());
    assertThat(saved.getActivityArea().getEmdArea().getId()).isEqualTo(command.emdId());
    assertThat(saved.getActivityArea().getAuthenticationAt()).isNotNull();
  }

  @Test
  @DisplayName("회원가입 - 실패 테스트(이메일 인증 X)")
  void 이메일_인증을_하지않은_사용자는_회원가입을_할_수_없다() {
    // given
    CreateMemberCommand command = defaultCreateMemberCommand();

    // when & then
    assertThatThrownBy(() -> memberService.signupProcess(command))
        .isInstanceOf(ApplicationException.class)
        .hasMessage(ApplicationError.UNVERIFIED_EMAIL.getMessage());
  }

  @Test
  @DisplayName("회원가입 - 실패 테스트(중복된 이메일)")
  void 동일한_이메일_계정이_존재하면_사용자는_회원가입을_할_수_없다() {
    // given
    String email = "test@email.com";
    mailVerificationStore.saveVerified(email, "true", 5);

    Member existing = customEmailMember(email);
    memberRepository.save(existing);

    CreateMemberCommand command = defaultCreateMemberCommand();

    // when & then
    assertThatThrownBy(() -> memberService.signupProcess(command))
        .isInstanceOf(ApplicationException.class)
        .hasMessage(ApplicationError.ALREADY_EXISTS_EMAIL.getMessage());
  }

  @Test
  @DisplayName("내 프로필 조회 - 성공 테스트")
  void 회원은_자신의_프로필을_조회할_수_있다() {
    // given
    Member member = defaultMember();
    memberRepository.save(member);
    member.updateActivityArea(defaultActivityArea());
    ReadProfileQuery query = ReadProfileQuery.of(member.getId());

    // when
    MemberProfileResponse response = memberService.readProfileProcess(query);

    // then
    assertThat(response.getMemberId()).isEqualTo(member.getId());
    assertThat(response.getNickname()).isEqualTo(member.getNickname());
    assertThat(response.getProfileImageUrl()).isEqualTo(member.getProfileImageUrl());
    assertThat(response.getArea().emdId()).isEqualTo(member.getActivityArea().getEmdArea().getId());
    assertThat(response.getArea().isAuthentication()).isEqualTo(member.getActivityArea().isValidAuthentication());
  }

  @Test
  @DisplayName("임시 비밀번호 발급 - 성공 테스트")
  void 이메일로_임시_비밀번호를_받아_비밀번호를_변경할_수_있다() {
    // given
    Member member = defaultMember();
    memberRepository.save(member);

    IssuePasswordCommand command = defaultIssuePasswordCommand();
    String tempPassword = "temp-password";
    given(mailService.sendTempPasswordProcess(command.email())).willReturn(tempPassword);

    // when
    memberService.issueTempPasswordProcess(command);

    // then
    assertThat(member.getPassword()).isNotEqualTo(defaultMember().getPassword());
    assertThat(passwordEncoder.matches(tempPassword, member.getPassword())).isTrue();
  }

  @Test
  @DisplayName("임시 비밀번호 발급 - 실패 테스트(존재하지 않는 이메일)")
  void 존재하지_않는_이메일이면_임시_비밀번호_발급에_실패한다() {
    // given
    String notExistsEmail = "unknown@email.com";
    IssuePasswordCommand command = new IssuePasswordCommand(notExistsEmail);

    // when & then
    assertThatThrownBy(() -> memberService.issueTempPasswordProcess(command))
        .isInstanceOf(ApplicationException.class)
        .hasMessage(ApplicationError.NOT_EXISTS_MEMBER.getMessage());
  }

  @Test
  @DisplayName("비밀번호 변경 - 성공 테스트")
  void 기존_비밀번호가_일치하면_비밀번호를_변경할_수_있다() {
    // given
    String password = "password";
    String newPassword = "new-password";
    Member member = encryptPasswordMember(passwordEncoder.encode(password));
    memberRepository.save(member);

    ChangePasswordCommand command = customChangePasswordCommand(member.getId(), password, newPassword);

    // when
    memberService.changePasswordProcess(command);

    // then
    assertThat(passwordEncoder.matches(newPassword, member.getPassword())).isTrue();
  }

  @Test
  @DisplayName("비밀번호 변경 - 실패 테스트(기존 비밀번호 불일치)")
  void 기존_비밀번호가_일치하지_않으면_비밀번호를_변경할_수_없다() {
    // given
    String password = "password";
    String encoded = passwordEncoder.encode(password);
    System.out.println("ENCODED: " + encoded);


    String wrongOldPassword = "wrong-password";
    String newPassword = "new-password";

    Member member = encryptPasswordMember(passwordEncoder.encode(password));
    memberRepository.save(member);

    ChangePasswordCommand command = customChangePasswordCommand(member.getId(), wrongOldPassword, newPassword);

    // when & then
    assertThatThrownBy(() -> memberService.changePasswordProcess(command))
        .isInstanceOf(ApplicationException.class)
        .hasMessage(ApplicationError.INVALID_OLD_PASSWORD.getMessage());
  }
}
