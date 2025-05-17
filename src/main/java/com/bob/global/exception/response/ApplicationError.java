package com.bob.global.exception.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ApplicationError {

  // 사용자 예외
  UNVERIFIED_EMAIL("E101", "이메일 인증이 완료되지 않았습니다.", HttpStatus.BAD_REQUEST),
  ALREADY_EXISTS_EMAIL("E102", "해당 이메일로 가입된 계정이 존재합니다.", HttpStatus.CONFLICT),
  EXPIRED_MAIL_CODE("E111", "인증 코드가 만료되었습니다.", HttpStatus.GONE),
  INVALID_MAIL_CODE("E112", "입력하신 인증 코드가 일치하지 않습니다.", HttpStatus.BAD_REQUEST),
  ;

  private String code;
  private String message;
  private HttpStatus status;
}
