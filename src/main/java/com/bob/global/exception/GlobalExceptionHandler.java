package com.bob.global.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import com.bob.global.exception.exceptions.ApplicationException;
import com.bob.global.exception.response.ApplicationError;
import com.bob.global.exception.response.ErrorResponse;

@RestControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(ApplicationException.class)
  public ResponseEntity<ErrorResponse> handleApplicationException(ApplicationException ex) {
    ApplicationError error = ex.getError();

    ErrorResponse response = new ErrorResponse(
        error.getCode(),
        error.getMessage()
    );

    return ResponseEntity.status(error.getStatus()).body(response);
  }
}

