package com.bob.infra.mail;

import static com.bob.global.exception.response.ApplicationError.EXPIRED_MAIL_CODE;
import static com.bob.global.exception.response.ApplicationError.INVALID_MAIL_CODE;

import com.bob.domain.member.service.port.MailService;
import com.bob.domain.member.service.port.MailVerificationStore;
import com.bob.global.exception.exceptions.ApplicationException;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class GoogleMailService implements MailService {

  private final JavaMailSender mailSender;
  private final SimpleMailMessage message;
  private final MailVerificationStore mailVerificationStore;

  @Override
  public void sendMailProcess(String email) {
    String verificationCode = generateCode();
    mailVerificationStore.saveCode(email, verificationCode, 3);

    message.setText("인증코드 : " + verificationCode);
    message.setTo(email);
    mailSender.send(message);
  }

  @Override
  public void verifyMailProcess(String email, String code) {
    String storedCode = mailVerificationStore.getCode(email)
        .orElseThrow(() -> new ApplicationException(EXPIRED_MAIL_CODE));

    if (!storedCode.equals(code)) {
      throw new ApplicationException(INVALID_MAIL_CODE);
    }

    mailVerificationStore.saveVerified(email, "true", 10);
    mailVerificationStore.deleteCode(email);
  }
}
