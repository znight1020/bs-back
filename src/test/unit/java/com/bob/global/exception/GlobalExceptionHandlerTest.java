package com.bob.global.exception;

import static org.assertj.core.api.Assertions.assertThat;

import com.bob.global.exception.exceptions.ApplicationException;
import com.bob.global.exception.response.ApplicationError;
import com.bob.global.exception.response.ErrorResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

@DisplayName("애플리케이션 전역 예외 핸들러 호출 테스트")
@ExtendWith(MockitoExtension.class)
public class GlobalExceptionHandlerTest {

  @InjectMocks
  private GlobalExceptionHandler handler;

  @Test
  @DisplayName("애플리케이션 전역 예외 핸들러 호출 테스트")
  void 애플리케이션_예외가_발생하면_정해진_형식의_응답이_생성된다() {
    // given
    ApplicationException ex = new ApplicationException(ApplicationError.ALREADY_EXISTS_EMAIL);

    // when
    ResponseEntity<ErrorResponse> response = handler.handleApplicationException(ex);

    // then
    assertThat(response.getStatusCode()).isEqualTo(ApplicationError.ALREADY_EXISTS_EMAIL.getStatus());
    assertThat(response.getBody()).isNotNull();
    assertThat(response.getBody().code()).isEqualTo(ex.getError().getCode());
    assertThat(response.getBody().message()).isEqualTo(ex.getError().getMessage());
  }
}
