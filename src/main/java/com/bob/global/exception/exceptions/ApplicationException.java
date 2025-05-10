package com.bob.global.exception.exceptions;

import lombok.Getter;
import com.bob.global.exception.response.ApplicationError;

@Getter
public class ApplicationException extends RuntimeException {

  private final ApplicationError error;

  public ApplicationException(ApplicationError error) {
    super(error.getMessage());
    this.error = error;
  }
}
