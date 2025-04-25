package toy.bookswap.global.infra.mail;

import java.time.Duration;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class GoogleMailService implements MailService {

  private final JavaMailSender mailSender;
  private final SimpleMailMessage message;
  private final StringRedisTemplate redisTemplate;

  @Override
  public void sendMailProcess(String email) {
    String verificationCode = generateCode();
    saveEmailData(email, verificationCode, 3);
    message.setText("인증코드 : " + verificationCode);
    message.setTo(email);
    mailSender.send(message);
  }

  @Override
  public void verifyMailProcess(String email, String code) {
    // TODO - 공통 Exception 처리
    String storedCode = getStoredCode(email)
        .orElseThrow(() -> new RuntimeException("인증 코드가 만료되었습니다."));

    if (!storedCode.equals(code)) {
      throw new RuntimeException("인증 코드가 유효하지 않습니다.");
    }
    saveEmailData("email-verified:" + email, "true", 10);
    deleteEmailData(email);
  }

  private void saveEmailData(String key, String data, int time) {
    redisTemplate.opsForValue().set(key, data, Duration.ofMinutes(time));
  }

  private void deleteEmailData(String key) {
    redisTemplate.delete(key);
  }

  private Optional<String> getStoredCode(String key) {
    return Optional.ofNullable(redisTemplate.opsForValue().get(key));
  }
}
