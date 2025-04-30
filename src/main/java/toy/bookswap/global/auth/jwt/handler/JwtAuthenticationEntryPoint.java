package toy.bookswap.global.auth.jwt.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import toy.bookswap.global.exception.exceptions.ApplicationAuthenticationException;
import toy.bookswap.global.exception.response.AuthenticationError;

@RequiredArgsConstructor
@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

  private final ObjectMapper objectMapper;

  @Override
  public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException {
    setHeader(response);
    String responseData = objectMapper.writeValueAsString(setBody(exception));
    response.getWriter().print(responseData);
  }

  private void setHeader(HttpServletResponse response) {
    response.setContentType("application/json; charset=UTF-8");
    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
  }

  private Map<String, Object> setBody(AuthenticationException e) {
    if (e instanceof ApplicationAuthenticationException exception) {
      return createCustomErrorResponse(exception);
    }
    return createDefaultErrorResponse();
  }

  private Map<String, Object> createCustomErrorResponse(ApplicationAuthenticationException authException) {
    AuthenticationError error = authException.getError();
    return Map.of(
        "code", error.getCode(),
        "message", error.getMessage()
    );
  }

  private Map<String, Object> createDefaultErrorResponse() {
    AuthenticationError error = AuthenticationError.FAILED_AUTHENTICATION;
    return Map.of(
        "code", error.getCode(),
        "message", error.getMessage()
    );
  }
}
