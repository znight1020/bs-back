package toy.bookswap.global.utils;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.*;
import static org.mockito.Mockito.mock;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("Cookie 유틸 테스트")
class CookieUtilsTests {

  @Test
  @DisplayName("Cookie 조회 - 성공 테스트")
  void 쿠키를_정상적으로_조회할_수_있다() {
    // given
    HttpServletRequest request = mock(HttpServletRequest.class);
    Cookie cookie = new Cookie("testName", "testValue");
    given(request.getCookies()).willReturn(new Cookie[]{cookie});

    // when
    String result = CookieUtils.getCookie(request, "testName");

    // then
    assertThat(result).isEqualTo("testValue");
  }

  @Test
  @DisplayName("Cookie 조회 - 실패 테스트(Null Cookie)")
  void 쿠키가_없으면_null을_반환한다() {
    // given
    HttpServletRequest request = mock(HttpServletRequest.class);
    given(request.getCookies()).willReturn(null);

    // when
    String result = CookieUtils.getCookie(request, "testName");

    // then
    assertThat(result).isNull();
  }

  @Test
  @DisplayName("Cookie 조회 - 실패 테스트(이름 불일치)")
  void 쿠키_이름이_다르면_null을_반환한다() {
    // given
    HttpServletRequest request = mock(HttpServletRequest.class);
    Cookie cookie = new Cookie("otherName", "otherValue");
    given(request.getCookies()).willReturn(new Cookie[]{cookie});

    // when
    String result = CookieUtils.getCookie(request, "testName");

    // then
    assertThat(result).isNull();
  }

  @Test
  @DisplayName("Cookie 헤더 추가 - 성공 테스트")
  void 쿠키를_응답에_추가할_수_있다() {
    // given
    HttpServletResponse response = mock(HttpServletResponse.class);

    // when
    CookieUtils.addCookie(response, "testName", "testValue", 3600);

    // then
    then(response).should().addHeader(eq("Set-Cookie"), contains("testName=testValue"));
  }
}
