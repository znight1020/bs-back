package com.bob.domain.member.service;

import static com.bob.global.exception.response.ApplicationError.ALREADY_EXISTS_EMAIL;
import static com.bob.global.exception.response.ApplicationError.NOT_EXISTS_MEMBER;
import static com.bob.global.exception.response.ApplicationError.UNVERIFIED_EMAIL;
import static com.bob.support.fixture.command.MemberCommandFixture.defaultCreateMemberCommand;
import static com.bob.support.fixture.command.MemberCommandFixture.defaultIssuePasswordCommand;
import static com.bob.support.fixture.domain.EmdAreaFixture.defaultEmdArea;
import static com.bob.support.fixture.domain.MemberFixture.defaultMember;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;

import com.bob.domain.area.entity.EmdArea;
import com.bob.domain.area.service.reader.AreaReader;
import com.bob.domain.member.command.CreateMemberCommand;
import com.bob.domain.member.command.IssuePasswordCommand;
import com.bob.domain.member.entity.Member;
import com.bob.domain.member.repository.MemberRepository;
import com.bob.domain.member.service.port.MailService;
import com.bob.domain.member.service.port.MailVerificationStore;
import com.bob.domain.member.service.reader.MemberReader;
import com.bob.global.exception.exceptions.ApplicationException;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@DisplayName("사용자 회원가입 테스트")
@ExtendWith(MockitoExtension.class)
class MemberServiceTest {

  @InjectMocks
  private MemberService memberService;

  @Mock
  private MemberRepository memberRepository;

  @Mock
  private MemberReader memberReader;

  @Mock
  private BCryptPasswordEncoder encoder;

  @Mock
  private MailService mailService;

  @Mock
  private MailVerificationStore mailVerificationStore;

  @Mock
  private AreaReader areaReader;

  @Test
  @DisplayName("회원가입 - 성공 테스트")
  void 회원가입을_진행할_수_있다() {
    // given
    CreateMemberCommand command = defaultCreateMemberCommand();
    EmdArea dummyEmdArea = defaultEmdArea();
    String encodedPassword = "$2a$10";

    given(encoder.encode(command.password())).willReturn(encodedPassword);
    given(mailVerificationStore.getVerified(command.email())).willReturn(Optional.of("true"));
    given(areaReader.readEmdArea(command.emdId())).willReturn(dummyEmdArea);

    ArgumentCaptor<Member> captor = ArgumentCaptor.forClass(Member.class);

    // when
    memberService.signupProcess(command);

    // then
    then(mailVerificationStore).should().getVerified(command.email());
    then(mailVerificationStore).should().deleteVerified(command.email());
    then(memberRepository).should(times(1)).save(captor.capture());

    Member saved = captor.getValue();
    assertThat(saved.getEmail()).isEqualTo(command.email());
    assertThat(saved.getNickname()).isEqualTo(command.nickname());
    assertThat(saved.getPassword()).isEqualTo(encodedPassword);
    assertThat(saved.getActivityArea().getEmdArea().getId()).isEqualTo(command.emdId());
    assertThat(saved.getActivityArea().getAuthenticationAt()).isNotNull();
  }

  @Test
  @DisplayName("회원가입 - 실패 테스트(이메일 인증 X)")
  void 이메일_인증이_되지_않으면_회원가입에_실패한다() {
    // given
    CreateMemberCommand command = defaultCreateMemberCommand();
    given(mailVerificationStore.getVerified(command.email())).willReturn(Optional.empty());

    // when & then
    assertThatThrownBy(() -> memberService.signupProcess(command))
        .isInstanceOf(RuntimeException.class)
        .hasMessage(UNVERIFIED_EMAIL.getMessage());
  }

  @Test
  @DisplayName("회원가입 - 실패 테스트(이미 존재하는 이메일 계정)")
  void 이메일_계정이_존재하면_회원가입에_실패한다() {
    // given
    CreateMemberCommand command = defaultCreateMemberCommand();
    given(memberRepository.existsByEmail(command.email())).willReturn(true);

    // when & then
    assertThatThrownBy(() -> memberService.signupProcess(command))
        .isInstanceOf(RuntimeException.class)
        .hasMessage(ALREADY_EXISTS_EMAIL.getMessage());
  }

  @Test
  @DisplayName("임시 비밀번호 발급 - 성공 테스트")
  void 임시_비밀번호_발급을_할_수_있다() {
    // given
    Member member = defaultMember();
    String rawTempPassword = member.getPassword();
    String encodedTempPassword = "$2a$encodedTemp";

    IssuePasswordCommand command = new IssuePasswordCommand(member.getEmail());

    given(memberReader.readMemberByEmail(member.getEmail())).willReturn(member);
    given(mailService.sendTempPasswordProcess(member.getEmail())).willReturn(rawTempPassword);
    given(encoder.encode(rawTempPassword)).willReturn(encodedTempPassword);

    // when
    memberService.issueTempPasswordProcess(command);

    // then
    then(memberReader).should().readMemberByEmail(member.getEmail());
    then(mailService).should().sendTempPasswordProcess(member.getEmail());
    then(encoder).should().encode(rawTempPassword);
    assertThat(member.getPassword()).isEqualTo(encodedTempPassword);
  }

  @Test
  @DisplayName("임시 비밀번호 발급 - 실패 테스트(존재하지 않는 이메일)")
  void 이메일_계정이_존재하지_않으면_임시_비밀번호_발급에_실패한다() {
    // given
    IssuePasswordCommand command = defaultIssuePasswordCommand();
    given(memberReader.readMemberByEmail(command.email())).willThrow(new ApplicationException(NOT_EXISTS_MEMBER));

    // when & then
    assertThatThrownBy(() -> memberService.issueTempPasswordProcess(command))
        .isInstanceOf(RuntimeException.class)
        .hasMessage(NOT_EXISTS_MEMBER.getMessage());
  }
}
