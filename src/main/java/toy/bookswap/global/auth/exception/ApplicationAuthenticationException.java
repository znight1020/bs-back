package toy.bookswap.global.auth.exception;

import lombok.Getter;
import org.springframework.security.core.AuthenticationException;

@Getter
public class ApplicationAuthenticationException extends AuthenticationException {

  private final AuthenticationError error;

  public ApplicationAuthenticationException(AuthenticationError error) {
    super(error.getMessage());
    this.error = error;
  }
}
