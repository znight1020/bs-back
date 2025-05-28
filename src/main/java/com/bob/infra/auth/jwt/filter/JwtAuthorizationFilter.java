package com.bob.infra.auth.jwt.filter;

import static com.bob.global.exception.response.AuthenticationError.FAILED_VERIFY_TOKEN;
import static com.bob.global.exception.response.AuthenticationError.IS_EXPIRED_TOKEN;
import static com.bob.global.exception.response.AuthenticationError.IS_NOT_EXIST_TOKEN;

import com.bob.infra.auth.response.MemberDetails;
import com.bob.infra.config.registry.OptionalRegistry;
import com.bob.infra.config.registry.PermitAllRegistry;
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
import com.bob.global.exception.exceptions.ApplicationAuthenticationException;
import com.bob.infra.auth.jwt.JwtProvider;
import com.bob.infra.auth.jwt.handler.JwtAuthenticationEntryPoint;
import com.bob.global.utils.web.CookieUtils;

@RequiredArgsConstructor
@Component
public class JwtAuthorizationFilter extends OncePerRequestFilter {

  @Value("${jwt.cookie-name}")
  private String COOKIE_NAME;

  private final JwtAuthenticationEntryPoint jwtAuthEntryPoint;
  private final PermitAllRegistry registry;
  private final OptionalRegistry optionalRegistry;
  private final JwtProvider jwtProvider;

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
    if (registry.isWhiteList(request)) {
      filterChain.doFilter(request, response);
      return;
    }

    if (optionalRegistry.isOptionalAuth(request) && CookieUtils.getCookie(request, COOKIE_NAME) == null) {
      filterChain.doFilter(request, response);
      return;
    }

    String accessToken = CookieUtils.getCookie(request, COOKIE_NAME);
    if (accessToken == null) {
      jwtAuthEntryPoint.commence(request, response, new ApplicationAuthenticationException(IS_NOT_EXIST_TOKEN));
      return;
    }

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
}
