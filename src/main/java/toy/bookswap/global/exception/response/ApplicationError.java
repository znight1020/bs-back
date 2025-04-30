package toy.bookswap.global.exception.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ApplicationError {

  // 사용자 예외
  EMAIL_IS_NOT_VERIFIED("E101", "이메일 인증이 완료되지 않았습니다.", HttpStatus.BAD_REQUEST),
  EMAIL_ALREADY_EXISTS("E102", "해당 이메일로 가입된 계정이 존재합니다.", HttpStatus.CONFLICT),
  MAIL_CODE_EXPIRED("E111", "인증 코드가 만료되었습니다.", HttpStatus.GONE),
  MAIL_CODE_INVALIDATE("E112", "입력하신 인증 코드가 일치하지 않습니다.", HttpStatus.BAD_REQUEST),
  ;

  private String code;
  private String message;
  private HttpStatus status;
}
