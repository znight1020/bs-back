package toy.bookswap.global.exception.exceptions;

import lombok.Getter;
import toy.bookswap.global.exception.response.ApplicationError;

@Getter
public class ApplicationException extends RuntimeException {

  private final ApplicationError error;

  public ApplicationException(ApplicationError error) {
    super(error.getMessage());
    this.error = error;
  }
}
