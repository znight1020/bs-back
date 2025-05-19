package com.bob.infra.config.registry;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpServletRequest;

@DisplayName("요청 URL 화이트리스트 테스트")
@ExtendWith(MockitoExtension.class)
class PermitAllRegistryTest {

  @InjectMocks
  private PermitAllRegistry registry;

  @Test
  @DisplayName("화이트리스트 포함 요청 URL")
  void 화이트리스트에_포함되면_true를_반환한다() {
    // given
    MockHttpServletRequest request = new MockHttpServletRequest();
    request.setServletPath("/auth/email");
    request.setMethod("POST");

    // when
    boolean result = registry.isWhiteList(request);

    // then
    assertThat(result).isTrue();
  }

  @Test
  @DisplayName("화이트리스트 미포함 요청 URL")
  void 화이트리스트에_포함되지_않으면_false를_반환한다() {
    // given
    MockHttpServletRequest request = new MockHttpServletRequest();
    request.setServletPath("/posts");
    request.setMethod("DELETE");

    // when
    boolean result = registry.isWhiteList(request);

    // then
    assertThat(result).isFalse();
  }
}
