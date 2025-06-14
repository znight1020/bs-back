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
  UN_SUPPORTED_BOOK_STATUS("E003", "지원하지 않는 도서 상태입니다.", HttpStatus.BAD_REQUEST),
  IS_SAME_REQUEST("E004", "변경 사항이 없습니다.", HttpStatus.BAD_REQUEST),
  VALIDATION_ERROR("E005", "요청 값이 올바르지 않습니다.", HttpStatus.BAD_REQUEST),
  TYPE_MISMATCH("E006", "요청 형식이 올바르지 않습니다.", HttpStatus.BAD_REQUEST),

  // 사용자 예외
  UNVERIFIED_EMAIL("E101", "이메일 인증이 완료되지 않았습니다.", HttpStatus.BAD_REQUEST),
  ALREADY_EXISTS_EMAIL("E102", "해당 이메일로 가입된 계정이 존재합니다.", HttpStatus.CONFLICT),
  NOT_EXISTS_MEMBER("E103", "사용자를 찾을 수 없습니다.", HttpStatus.BAD_REQUEST),
  INVALID_OLD_PASSWORD("E104", "비밀번호가 틀립니다.", HttpStatus.BAD_REQUEST),

  EXPIRED_MAIL_CODE("E111", "인증 코드가 만료되었습니다.", HttpStatus.GONE),
  INVALID_MAIL_CODE("E112", "입력하신 인증 코드가 일치하지 않습니다.", HttpStatus.BAD_REQUEST),

  // 지역 예외
  NOT_EXISTS_AREA("E201", "존재하지 않는 읍/면/동 입니다.", HttpStatus.BAD_REQUEST),
  NOT_EXISTS_ACTIVITY_AREA("E201", "활동지역을 찾을 수 없습니다.", HttpStatus.BAD_REQUEST),
  INVALID_AREA_AUTHENTICATION("E211", "현재 위치를 인증할 수 없습니다.", HttpStatus.BAD_REQUEST),

  // 게시글 예외
  NOT_VERIFIED_MEMBER("E301", "위치 인증을 하지 않은 사용자는 게시글을 작성할 수 없습니다.", HttpStatus.FORBIDDEN),
  NOT_EXIST_POST("E302", "게시글을 찾을 수 없습니다.", HttpStatus.BAD_REQUEST),
  NOT_POST_OWNER("E303", "게시글 작성자가 아닙니다.", HttpStatus.FORBIDDEN),
  ALREADY_POST_FAVORITE("E304", "이미 좋아요한 게시글입니다.", HttpStatus.BAD_REQUEST),
  INVALID_POST_FAVORITE("E305", "좋아요 하지 않은 게시글입니다.", HttpStatus.BAD_REQUEST),

  // 채팅 예외
  IS_SAME_CHAT_MEMBER("E401", "자신과의 채팅은 불가능합니다.", HttpStatus.BAD_REQUEST)
  ;

  private String code;
  private String message;
  private HttpStatus status;
}
