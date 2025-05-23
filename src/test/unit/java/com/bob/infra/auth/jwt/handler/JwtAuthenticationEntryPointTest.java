package com.bob.infra.auth.jwt.handler;

import static org.assertj.core.api.Assertions.assertThat;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.test.util.ReflectionTestUtils;
import com.bob.global.exception.exceptions.ApplicationAuthenticationException;
import com.bob.global.exception.response.AuthenticationError;

@DisplayName("JWT 예외 핸들러 테스트")
@ExtendWith(MockitoExtension.class)
class JwtAuthenticationEntryPointTest {

  @InjectMocks
  private JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

  private final String CONTENT_TYPE = "application/json; charset=UTF-8";
  private final ObjectMapper objectMapper = new ObjectMapper();
  private MockHttpServletResponse response;

  @BeforeEach
  void setUp() {
    ReflectionTestUtils.setField(jwtAuthenticationEntryPoint, "objectMapper", objectMapper);
    response = new MockHttpServletResponse();
  }

  @Test
  @DisplayName("예외 반환 테스트 - 인증 실패 전역 예외")
  void AuthenticationException_발생시_기본_예외를_반환한다() throws IOException {
    // given
    AuthenticationException exception = new AuthenticationException("인증 실패") {};

    // when
    jwtAuthenticationEntryPoint.commence(null, response, exception);

    // then
    String content = response.getContentAsString();
    assertThat(response.getStatus()).isEqualTo(HttpServletResponse.SC_UNAUTHORIZED);
    assertThat(response.getContentType()).isEqualTo(CONTENT_TYPE);
    assertThat(content).contains(AuthenticationError.FAILED_AUTHENTICATION.getCode());
    assertThat(content).contains(AuthenticationError.FAILED_AUTHENTICATION.getMessage());
  }

  @Test
  @DisplayName("예외 반환 테스트 - 특정 예외")
  void ApplicationAuthenticationException_발생_시_커스텀_예외를_반환한다() throws IOException {
    // given
    AuthenticationException exception = new ApplicationAuthenticationException(AuthenticationError.IS_EXPIRED_TOKEN) {};

    // when
    jwtAuthenticationEntryPoint.commence(null, response, exception);

    // then
    String content = response.getContentAsString();
    assertThat(response.getStatus()).isEqualTo(HttpServletResponse.SC_UNAUTHORIZED);
    assertThat(response.getContentType()).isEqualTo(CONTENT_TYPE);
    assertThat(content).contains(AuthenticationError.IS_EXPIRED_TOKEN.getCode());
    assertThat(content).contains(AuthenticationError.IS_EXPIRED_TOKEN.getMessage());
  }
}
