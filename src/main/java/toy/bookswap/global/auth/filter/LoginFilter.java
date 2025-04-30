package toy.bookswap.global.auth.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import toy.bookswap.global.exception.exceptions.ApplicationAuthenticationException;
import toy.bookswap.global.exception.response.AuthenticationError;
import toy.bookswap.global.auth.filter.request.LoginRequest;
import toy.bookswap.global.auth.jwt.JwtProvider;
import toy.bookswap.global.auth.response.MemberDetails;
import toy.bookswap.global.utils.CookieUtils;

@RequiredArgsConstructor
public class LoginFilter extends UsernamePasswordAuthenticationFilter {

  private final AuthenticationManager authenticationManager;
  private final AuthenticationEntryPoint authenticationEntryPoint;
  private final JwtProvider jwtProvider;
  private final ObjectMapper objectMapper;

  @Override
  public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
    LoginRequest loginRequest = readLoginData(request);
    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(loginRequest.email(), loginRequest.password());
    return authenticationManager.authenticate(authToken);
  }

  @Override
  protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authentication) {
    MemberDetails principal = (MemberDetails) authentication.getPrincipal();
    Long memberId = principal.getId();
    String accessToken = jwtProvider.generateAccessToken(memberId);
    String refreshToken = jwtProvider.generateRefreshToken(memberId);
    CookieUtils.addCookie(response, "Authorization", accessToken, 216000);
    CookieUtils.addCookie(response, "refresh", refreshToken, 216000);
    response.setStatus(HttpStatus.OK.value());
  }

  @Override
  protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {
    authenticationEntryPoint.commence(
        request,
        response,
        new ApplicationAuthenticationException(AuthenticationError.FAILED_AUTHENTICATION) {}
    );
  }

  private LoginRequest readLoginData(HttpServletRequest request) {
    try {
      return objectMapper.readValue(request.getInputStream(), LoginRequest.class);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }
}
