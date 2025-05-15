package com.bob.global.props;

import static org.assertj.core.api.Assertions.assertThat;

import com.bob.global.utils.CookieUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletResponse;

class CookiePropsTest {

  @Test
  @DisplayName("SameSite 설정")
  void CookieProps_클래스를_사용해_sameSite_값을_설정할_수_있다() {
    // given
    CookieProps cookieProps = new CookieProps();

    // when
    cookieProps.setSameSite("NONE");

    // then
    assertThat(cookieProps.getSameSite()).isEqualTo("NONE");

    MockHttpServletResponse response = new MockHttpServletResponse();
    CookieUtils.addCookie(response, "name", "value", 1000);
    assertThat(response.getHeader("Set-Cookie")).contains("SameSite=NONE");
  }
}