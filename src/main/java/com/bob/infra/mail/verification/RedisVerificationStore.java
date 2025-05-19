package com.bob.infra.mail.verification;

import com.bob.domain.member.service.port.MailVerificationStore;
import java.time.Duration;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class RedisVerificationStore implements MailVerificationStore {

  private final StringRedisTemplate redisTemplate;

  @Override
  public void saveVerified(String email, String value, int expireMinutes) {
    redisTemplate.opsForValue().set(emailVerifiedKey(email), value, Duration.ofMinutes(expireMinutes));
  }

  @Override
  public Optional<String> getVerified(String email) {
    return Optional.ofNullable(redisTemplate.opsForValue().get(emailVerifiedKey(email)));
  }

  @Override
  public void deleteVerified(String email) {
    redisTemplate.delete(emailVerifiedKey(email));
  }

  @Override
  public void saveCode(String email, String code, int expireMinutes) {
    redisTemplate.opsForValue().set(emailCodeKey(email), code, Duration.ofMinutes(expireMinutes));
  }

  @Override
  public Optional<String> getCode(String email) {
    return Optional.ofNullable(redisTemplate.opsForValue().get(emailCodeKey(email)));
  }

  @Override
  public void deleteCode(String email) {
    redisTemplate.delete(emailCodeKey(email));
  }

  private String emailVerifiedKey(String email) {
    return "email-verified:" + email;
  }

  private String emailCodeKey(String email) {
    return "email-code:" + email;
  }
}
