package com.bob.infra.mail.verification;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

import com.bob.infra.redis.adapter.RedisMailVerificationStore;
import java.time.Duration;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

@DisplayName("REDIS 메일 인증 테스트")
@ExtendWith(MockitoExtension.class)
class RedisVerificationStoreTest {

  @Mock
  private StringRedisTemplate redisTemplate;

  @Mock
  private ValueOperations<String, String> valueOperations;

  @InjectMocks
  private RedisMailVerificationStore redisVerificationStore;

  @Test
  @DisplayName("인증 상태 저장 테스트")
  void 이메일_인증상태를_저장한다() {
    // given
    String email = "user@email.com";
    String value = "true";
    int minutes = 3;
    given(redisTemplate.opsForValue()).willReturn(valueOperations);

    // when
    redisVerificationStore.saveVerified(email, value, minutes);

    // then
    then(valueOperations).should().set("email-verified:" + email, value, Duration.ofMinutes(minutes));
  }

  @Test
  @DisplayName("인증 상태 조회 테스트")
  void 이메일_인증상태를_조회한다() {
    // given
    String email = "user@email.com";
    given(redisTemplate.opsForValue()).willReturn(valueOperations);
    given(valueOperations.get("email-verified:" + email)).willReturn("true");

    // when
    Optional<String> result = redisVerificationStore.getVerified(email);

    // then
    assertThat(result).isPresent().contains("true");
  }

  @Test
  @DisplayName("인증 상태 삭제 테스트")
  void 이메일_인증상태를_삭제한다() {
    // given
    String email = "user@email.com";

    // when
    redisVerificationStore.deleteVerified(email);

    // then
    then(redisTemplate).should().delete("email-verified:" + email);
  }

  @Test
  @DisplayName("인증 코드 저장 테스트")
  void 인증코드를_저장한다() {
    // given
    String email = "user@email.com";
    String code = "123456";
    int minutes = 5;
    given(redisTemplate.opsForValue()).willReturn(valueOperations);

    // when
    redisVerificationStore.saveCode(email, code, minutes);

    // then
    then(valueOperations).should().set("email-code:" + email, code, Duration.ofMinutes(minutes));
  }

  @Test
  @DisplayName("인증 코드 조회 테스트")
  void 인증코드를_조회한다() {
    // given
    String email = "user@email.com";
    given(redisTemplate.opsForValue()).willReturn(valueOperations);
    given(valueOperations.get("email-code:" + email)).willReturn("123456");

    // when
    Optional<String> result = redisVerificationStore.getCode(email);

    // then
    assertThat(result).isPresent().contains("123456");
  }

  @Test
  @DisplayName("인증 코드 삭제 테스트")
  void 인증코드를_삭제한다() {
    // given
    String email = "user@email.com";

    // when
    redisVerificationStore.deleteCode(email);

    // then
    then(redisTemplate).should().delete("email-code:" + email);
  }
}
