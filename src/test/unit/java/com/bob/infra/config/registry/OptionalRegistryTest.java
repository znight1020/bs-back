package com.bob.infra.config.registry;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpServletRequest;

@DisplayName("선택적 인증 URL 테스트")
@ExtendWith(MockitoExtension.class)
class OptionalRegistryTest {

  @InjectMocks
  private OptionalRegistry registry;

  @Test
  @DisplayName("선택적 인증 포함 요청 URL")
  void optional_인증_URL에_포함되면_true를_반환한다() {
    // given
    MockHttpServletRequest request = new MockHttpServletRequest();
    request.setServletPath("/areas/authentication");
    request.setMethod("PATCH");

    // when
    boolean result = registry.isOptionalAuth(request);

    // then
    assertThat(result).isTrue();
  }

  @Test
  @DisplayName("선택적 인증 미포함 요청 URL")
  void optional_인증_URL에_포함되지_않으면_false를_반환한다() {
    // given
    MockHttpServletRequest request = new MockHttpServletRequest();
    request.setServletPath("/posts");
    request.setMethod("POST");

    // when
    boolean result = registry.isOptionalAuth(request);

    // then
    assertThat(result).isFalse();
  }
}
