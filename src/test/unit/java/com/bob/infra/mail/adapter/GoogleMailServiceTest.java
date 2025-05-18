package com.bob.infra.mail.adapter;

import static com.bob.support.fixture.auth.MailFixture.EMAIL;
import static com.bob.support.fixture.auth.MailFixture.INVALID_CODE;
import static com.bob.support.fixture.auth.MailFixture.VALID_CODE;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

import com.bob.domain.member.service.port.MailVerificationStore;
import com.bob.infra.mail.GoogleMailService;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

@DisplayName("Email 서비스 테스트")
@ExtendWith(MockitoExtension.class)
class GoogleMailServiceTest {

  @InjectMocks
  private GoogleMailService googleMailService;

  @Mock
  private JavaMailSender mailSender;

  @Mock
  private SimpleMailMessage simpleMailMessage;

  @Mock
  private MailVerificationStore mailVerificationStore;

  @Test
  @DisplayName("이메일 전송 - 성공 테스트")
  void 이메일_인증_코드를_전송할_수_있다() {
    // when
    googleMailService.sendMailProcess(EMAIL);

    // then
    then(mailSender).should().send(simpleMailMessage);
  }

  @Test
  @DisplayName("이메일 인증 코드 검증 - 성공 테스트")
  void 이메일_인증_코드를_검증할_수_있다() {
    // given
    given(mailVerificationStore.getCode(EMAIL)).willReturn(Optional.of(VALID_CODE));

    // when
    googleMailService.verifyMailProcess(EMAIL, VALID_CODE);

    // then
    then(mailVerificationStore).should().getCode(EMAIL);
    then(mailVerificationStore).should().saveVerified(EMAIL, "true", 10);
    then(mailVerificationStore).should().deleteCode(EMAIL);
  }

  @Test
  @DisplayName("인증 코드 검증 - 실패 테스트(인증 코드 불일치)")
  void 인증_코드가_일치하지_않으면_예외를_발생시킨다() {
    // given
    given(mailVerificationStore.getCode(EMAIL)).willReturn(Optional.of(INVALID_CODE));

    // when, then
    assertThatThrownBy(() -> googleMailService.verifyMailProcess(EMAIL, VALID_CODE))
        .isInstanceOf(RuntimeException.class)
        .hasMessage("입력하신 인증 코드가 일치하지 않습니다.");
  }

  @Test
  @DisplayName("인증 코드 만료 - 실패 테스트")
  void 인증_코드가_만료되었으면_예외를_발생시킨다() {
    // given
    given(mailVerificationStore.getCode(EMAIL)).willReturn(Optional.empty());

    // when, then
    assertThatThrownBy(() -> googleMailService.verifyMailProcess(EMAIL, VALID_CODE))
        .isInstanceOf(RuntimeException.class)
        .hasMessage("인증 코드가 만료되었습니다.");
  }
}
