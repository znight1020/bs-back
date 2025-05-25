package com.bob.global.exception.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ApplicationError {

  // 공통 예외
  UN_SUPPORTED_TYPE("E001", "지원하지 않는 형식입니다.", HttpStatus.BAD_REQUEST),
  UN_SUPPORTED_CATEGORY("E002", "지원하지 않는 카테고리입니다.", HttpStatus.BAD_REQUEST),

  // 사용자 예외
  UNVERIFIED_EMAIL("E101", "이메일 인증이 완료되지 않았습니다.", HttpStatus.BAD_REQUEST),
  ALREADY_EXISTS_EMAIL("E102", "해당 이메일로 가입된 계정이 존재합니다.", HttpStatus.CONFLICT),
  NOT_EXISTS_MEMBER("E103", "사용자를 찾을 수 없습니다.", HttpStatus.BAD_REQUEST),
  INVALID_OLD_PASSWORD("E104", "비밀번호가 틀립니다.", HttpStatus.BAD_REQUEST),
  IS_SAME_VALUE("E105", "변경 사항이 없습니다.", HttpStatus.BAD_REQUEST),

  EXPIRED_MAIL_CODE("E111", "인증 코드가 만료되었습니다.", HttpStatus.GONE),
  INVALID_MAIL_CODE("E112", "입력하신 인증 코드가 일치하지 않습니다.", HttpStatus.BAD_REQUEST),

  // 지역 예외
  NOT_EXISTS_AREA("E201", "존재하지 않는 읍/면/동 입니다.", HttpStatus.BAD_REQUEST),
  NOT_EXISTS_ACTIVITY_AREA("E201", "활동지역을 찾을 수 없습니다.", HttpStatus.BAD_REQUEST),

  INVALID_AREA_AUTHENTICATION("E211", "현재 위치를 인증할 수 없습니다.", HttpStatus.BAD_REQUEST)
  ;

  private String code;
  private String message;
  private HttpStatus status;
}
