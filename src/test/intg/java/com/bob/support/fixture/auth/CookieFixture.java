package com.bob.support.fixture.auth;

import jakarta.servlet.http.Cookie;

public class CookieFixture {

  public static final String SET_COOKIE_HEADER = "Set-Cookie";
  public static final String AUTH_COOKIE_NAME = "Authorization";
  public static final String AUTH_COOKIE_ACCESS_VALUE = "CTOKEN:access-token";
  public static final String AUTH_COOKIE_REFRESH_VALUE = "CTOKEN:refresh-token";
  public static final String ACCESS_VALUE = "access-token";
  public static final String REFRESH_VALUE = "refresh-token";
  public static final String AUTH_COOKIE = "Authorization=CTOKEN:access-token";

  public static Cookie defaultAuthCookie() {
    return new Cookie(AUTH_COOKIE_NAME, AUTH_COOKIE_ACCESS_VALUE);
  }

  public static Cookie nonMatchingCookie() {
    return new Cookie("otherName", "otherValue");
  }
}
