package com.bob.global.infra.mail;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

import java.time.Duration;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
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
  private StringRedisTemplate redisTemplate;

  @Mock
  private ValueOperations<String, String> valueOperations;

  @BeforeEach
  void setUp() {
    given(redisTemplate.opsForValue()).willReturn(valueOperations);
  }

  @Test
  @DisplayName("이메일 전송 - 성공 테스트")
  void 이메일_인증_코드를_전송할_수_있다() {
    // given
    String email = "test@email.com";

    // when
    googleMailService.sendMailProcess(email);

    // then
    then(mailSender).should().send(simpleMailMessage);
    then(redisTemplate).should().opsForValue();
  }

  @Test
  @DisplayName("이메일 인증 코드 검증 - 성공 테스트")
  void 이메일_인증_코드를_검증할_수_있다() {
    // given
    String email = "test@email.com";
    String code = "ABC123";
    given(redisTemplate.opsForValue()).willReturn(valueOperations);
    given(valueOperations.get(email)).willReturn(code);

    // when
    googleMailService.verifyMailProcess(email, code);

    // then
    then(valueOperations).should().get(email);
    then(valueOperations).should().set("email-verified:" + email, "true", Duration.ofMinutes(10));
    then(redisTemplate).should().delete(email);
  }

  @Test
  @DisplayName("인증 코드 검증 - 실패 테스트(인증 코드 불일치)")
  void 인증_코드가_일치하지_않으면_예외를_발생시킨다() {
    // given
    String email = "test@email.com";
    String storedCode = "XYZ789";
    String inputCode = "ABC123";

    given(redisTemplate.opsForValue().get(email)).willReturn(storedCode);

    // when, then
    assertThatThrownBy(() -> googleMailService.verifyMailProcess(email, inputCode))
        .isInstanceOf(RuntimeException.class)
        .hasMessage("입력하신 인증 코드가 일치하지 않습니다.");
  }

  @Test
  @DisplayName("인증 코드 만료 - 실패 테스트(인증 코드 만료)")
  void 인증_코드가_만료되었으면_예외를_발생시킨다() {
    // given
    String email = "test@email.com";
    given(redisTemplate.opsForValue().get(email)).willReturn(null);

    // when, then
    assertThatThrownBy(() -> googleMailService.verifyMailProcess(email, "any"))
        .isInstanceOf(RuntimeException.class)
        .hasMessage("인증 코드가 만료되었습니다.");
  }
}
