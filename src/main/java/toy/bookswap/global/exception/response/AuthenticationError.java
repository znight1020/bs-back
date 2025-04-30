package toy.bookswap.global.exception.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum AuthenticationError {
  FAILED_AUTHENTICATION("E001", "인증에 실패하였습니다."),
  FAILED_VERIFY_TOKEN("E002", "검증할 수 없는 토큰입니다."),
  IS_EXPIRED_TOKEN("E003", "만료된 토큰입니다."),
  IS_NOT_EXIST_TOKEN("E004", "토큰이 존재하지 않습니다.");

  private String code;
  private String message;
}

