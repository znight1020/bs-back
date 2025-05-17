package com.bob.global.exception;

import static org.assertj.core.api.Assertions.assertThat;

import com.bob.global.exception.exceptions.ApplicationException;
import com.bob.global.exception.response.ApplicationError;
import com.bob.global.exception.response.ErrorResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

@ExtendWith(MockitoExtension.class)
public class GlobalExceptionHandlerTest {

  @InjectMocks
  private GlobalExceptionHandler handler;

  @Test
  void handleApplicationException_shouldReturnExpectedErrorResponse() {
    // given
    ApplicationException ex = new ApplicationException(ApplicationError.ALREADY_EXISTS_EMAIL);

    // when
    ResponseEntity<ErrorResponse> response = handler.handleApplicationException(ex);

    // then
    assertThat(response.getStatusCode()).isEqualTo(ApplicationError.ALREADY_EXISTS_EMAIL.getStatus());
    assertThat(response.getBody()).isNotNull();
    assertThat(response.getBody().code()).isEqualTo("E102");
    assertThat(response.getBody().message()).isEqualTo("해당 이메일로 가입된 계정이 존재합니다.");
  }
}
