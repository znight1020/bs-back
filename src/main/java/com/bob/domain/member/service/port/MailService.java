package com.bob.domain.member.service.port;

import java.util.Random;
import java.util.stream.Collectors;

public interface MailService {

  void sendMailProcess(String email);

  void verifyMailProcess(String email, String code);

  default String generateCode() {
    String candidateChars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";

    Random random = new Random();
    return random.ints(6, 0, candidateChars.length())
        .mapToObj(candidateChars::charAt)
        .map(String::valueOf)
        .collect(Collectors.joining());
  }
}
