package toy.bookswap.global.exception;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import toy.bookswap.global.exception.exceptions.ApplicationException;
import toy.bookswap.global.exception.response.ApplicationError;
import toy.bookswap.global.exception.response.ErrorResponse;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
public class GlobalExceptionHandlerTest {

  @InjectMocks
  private GlobalExceptionHandler handler;

  @Test
  void handleApplicationException_shouldReturnExpectedErrorResponse() {
    // given
    ApplicationException ex = new ApplicationException(ApplicationError.EMAIL_ALREADY_EXISTS);

    // when
    ResponseEntity<ErrorResponse> response = handler.handleApplicationException(ex);

    // then
    assertThat(response.getStatusCode()).isEqualTo(ApplicationError.EMAIL_ALREADY_EXISTS.getStatus());
    assertThat(response.getBody()).isNotNull();
    assertThat(response.getBody().code()).isEqualTo("E102");
    assertThat(response.getBody().message()).isEqualTo("해당 이메일로 가입된 계정이 존재합니다.");
  }
}
