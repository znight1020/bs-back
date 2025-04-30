package toy.bookswap.global.auth.jwt.filter;

import static toy.bookswap.global.exception.response.AuthenticationError.FAILED_VERIFY_TOKEN;
import static toy.bookswap.global.exception.response.AuthenticationError.IS_EXPIRED_TOKEN;
import static toy.bookswap.global.exception.response.AuthenticationError.IS_NOT_EXIST_TOKEN;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import toy.bookswap.global.exception.exceptions.ApplicationAuthenticationException;
import toy.bookswap.global.auth.jwt.JwtProvider;
import toy.bookswap.global.auth.jwt.handler.JwtAuthenticationEntryPoint;
import toy.bookswap.global.auth.response.MemberDetails;
import toy.bookswap.global.utils.CookieUtils;

@RequiredArgsConstructor
@Component
public class JwtAuthorizationFilter extends OncePerRequestFilter {

  @Value("${jwt.token-prefix}")
  private String TOKEN_PREFIX;

  private final JwtAuthenticationEntryPoint jwtAuthEntryPoint;
  private final JwtProvider jwtProvider;

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
    if (isWhiteList(request)) {
      filterChain.doFilter(request, response);
      return;
    }

    String authorizationCookieValue = CookieUtils.getCookie(request, "Authorization");
    if (authorizationCookieValue == null || !authorizationCookieValue.startsWith(TOKEN_PREFIX)) {
      jwtAuthEntryPoint.commence(request, response, new ApplicationAuthenticationException(IS_NOT_EXIST_TOKEN));
      return;
    }

    String accessToken = authorizationCookieValue.substring(TOKEN_PREFIX.length() + 1);
    if (!jwtProvider.isVerified(accessToken)) {
      jwtAuthEntryPoint.commence(request, response, new ApplicationAuthenticationException(FAILED_VERIFY_TOKEN));
      return;
    }

    if (jwtProvider.isExpired(accessToken)) {
      jwtAuthEntryPoint.commence(request, response, new ApplicationAuthenticationException(IS_EXPIRED_TOKEN));
      return;
    }

    MemberDetails memberDetails = new MemberDetails(jwtProvider.getMemberId(accessToken));
    Authentication authentication = new UsernamePasswordAuthenticationToken(
        memberDetails, null, memberDetails.getAuthorities()
    );
    SecurityContextHolder.getContext().setAuthentication(authentication);
    filterChain.doFilter(request, response);
  }

  private boolean isWhiteList(HttpServletRequest request) {
    String URI = request.getRequestURI();
    return URI.startsWith("/auth") || URI.startsWith("/members/signup") || URI.startsWith("/h2-console");
  }
}
