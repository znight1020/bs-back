package toy.bookswap.global.exception.exceptions;

import lombok.Getter;
import org.springframework.security.core.AuthenticationException;
import toy.bookswap.global.exception.response.AuthenticationError;

@Getter
public class ApplicationAuthenticationException extends AuthenticationException {

  private final AuthenticationError error;

  public ApplicationAuthenticationException(AuthenticationError error) {
    super(error.getMessage());
    this.error = error;
  }
}
