package com.bob.infra.auth.jwt.filter;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.argThat;
import static org.mockito.BDDMockito.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static com.bob.global.exception.response.AuthenticationError.FAILED_VERIFY_TOKEN;
import static com.bob.global.exception.response.AuthenticationError.IS_EXPIRED_TOKEN;
import static com.bob.global.exception.response.AuthenticationError.IS_NOT_EXIST_TOKEN;

import jakarta.servlet.FilterChain;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.util.ReflectionTestUtils;
import com.bob.infra.auth.jwt.JwtProvider;
import com.bob.infra.auth.jwt.handler.JwtAuthenticationEntryPoint;
import com.bob.infra.auth.response.MemberDetails;
import com.bob.global.exception.exceptions.ApplicationAuthenticationException;
import com.bob.global.utils.CookieUtils;

@DisplayName("JWT 토큰 인증 필터 테스트")
@ExtendWith(MockitoExtension.class)
class JwtAuthorizationFilterTest {

  @InjectMocks
  private JwtAuthorizationFilter jwtAuthorizationFilter;

  @Mock
  private JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

  @Mock
  private JwtProvider jwtProvider;

  @Mock
  private HttpServletRequest request;

  @Mock
  private HttpServletResponse response;

  @Mock
  private FilterChain filterChain;

  @Mock
  private CookieUtils cookieUtils;

  @BeforeEach
  void setUp() {
    ReflectionTestUtils.setField(jwtAuthorizationFilter, "TOKEN_PREFIX", "TestTK");
    given(request.getRequestURI()).willReturn("");
  }

  @AfterEach
  void clearContext() {
    SecurityContextHolder.clearContext();
  }

  @Test
  @DisplayName("Token 인증 - 성공 테스트")
  void 토큰이_유효하다면_인증에_성공한다() throws Exception {
    // given
    given(request.getCookies()).willReturn(new Cookie[]{
        new Cookie("Authorization", "TestTK valid-token")
    });
    given(jwtProvider.isVerified("valid-token")).willReturn(true);
    given(jwtProvider.isExpired("valid-token")).willReturn(false);
    given(jwtProvider.getMemberId("valid-token")).willReturn(1L);

    // when
    jwtAuthorizationFilter.doFilterInternal(request, response, filterChain);

    // then
    assertThat(SecurityContextHolder.getContext().getAuthentication()).isNotNull();
    assertThat(SecurityContextHolder.getContext().getAuthentication().isAuthenticated()).isTrue();
    assertThat(SecurityContextHolder.getContext().getAuthentication().getPrincipal())
        .isInstanceOf(MemberDetails.class);

    then(filterChain).should().doFilter(request, response);
  }

  @Test
  @DisplayName("Token 인증 - 실패 테스트(Null Token)")
  void 토큰이_없는_경우_인증에_실패한다() throws Exception {
    // given
    given(request.getCookies()).willReturn(null);

    // when
    jwtAuthorizationFilter.doFilterInternal(request, response, filterChain);

    // then
    then(jwtAuthenticationEntryPoint).should()
        .commence(eq(request), eq(response),
            argThat(
                e -> ((ApplicationAuthenticationException) e).getError() == IS_NOT_EXIST_TOKEN
            )
        );
  }

  @Test
  @DisplayName("Token 인증 - 실패 테스트(Token 검증 실패)")
  void 토큰이_검증되지_않으면_인증에_실패한다() throws Exception {
    // given
    given(request.getCookies()).willReturn(new Cookie[]{
        new Cookie("Authorization", "TestTK invalid-token")
    });
    given(jwtProvider.isVerified("invalid-token")).willReturn(false);

    // when
    jwtAuthorizationFilter.doFilterInternal(request, response, filterChain);

    // then
    then(jwtAuthenticationEntryPoint).should()
        .commence(eq(request), eq(response),
            argThat(e ->
                ((ApplicationAuthenticationException) e).getError() == FAILED_VERIFY_TOKEN
            )
        );
  }

  @Test
  @DisplayName("Token 인증 - 실패 테스트(Token 만료)")
  void 토큰이_만료되면_인증에_실패한다() throws Exception {
    // given
    given(request.getCookies()).willReturn(new Cookie[]{
        new Cookie("Authorization", "TestTK expired-token")
    });
    given(jwtProvider.isVerified("expired-token")).willReturn(true);
    given(jwtProvider.isExpired("expired-token")).willReturn(true);

    // when
    jwtAuthorizationFilter.doFilterInternal(request, response, filterChain);

    // then
    then(jwtAuthenticationEntryPoint).should()
        .commence(eq(request), eq(response),
            argThat(
                e -> ((ApplicationAuthenticationException) e).getError() == IS_EXPIRED_TOKEN
            )
        );
  }

  @Test
  @DisplayName("JWT 인증 WhiteList 테스트")
  void expiredToken() throws Exception {
    // given
    given(request.getRequestURI()).willReturn("/auth");

    // when
    jwtAuthorizationFilter.doFilterInternal(request, response, filterChain);

    // then
    then(cookieUtils).shouldHaveNoInteractions();
  }

}
