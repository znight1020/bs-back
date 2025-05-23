package com.bob.infra.config;

import java.util.Properties;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;

@Configuration
public class MailConfig {

  @Value("${spring.mail.username}")
  private String username;

  @Value("${spring.mail.password}")
  private String password;

  @Bean
  public JavaMailSenderImpl mailSender() {
    JavaMailSenderImpl mailSender = new JavaMailSenderImpl();

    mailSender.setHost("smtp.gmail.com");
    mailSender.setPort(587);
    mailSender.setUsername(username);
    mailSender.setPassword(password);

    Properties props = mailSender.getJavaMailProperties();
    props.put("mail.smtp.auth", "true");
    props.put("mail.smtp.starttls.enable", "true");

    return mailSender;
  }

  @Bean
  public SimpleMailMessage defaultMailMessage() {
    SimpleMailMessage message = new SimpleMailMessage();
    message.setFrom(username);
    return message;
  }
}
