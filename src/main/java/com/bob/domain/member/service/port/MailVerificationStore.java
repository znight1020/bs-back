package com.bob.domain.member.service.port;

import java.util.Optional;

public interface MailVerificationStore {

  void saveVerified(String email, String value, int expireMinutes);

  Optional<String> getVerified(String email);

  void deleteVerified(String email);

  void saveCode(String email, String code, int expireMinutes);

  Optional<String> getCode(String email);

  void deleteCode(String email);
}
