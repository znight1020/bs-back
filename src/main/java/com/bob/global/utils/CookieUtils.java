package com.bob.global.utils;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;
import org.springframework.http.ResponseCookie;

public class CookieUtils {

  public static String getCookie(HttpServletRequest request, String name) {
    return Optional.ofNullable(request.getCookies())
        .flatMap(cookies -> Arrays.stream(cookies)
            .filter(cookie -> Objects.equals(name, cookie.getName()))
            .map(Cookie::getValue)
            .findAny()).orElse(null);
  }

  public static void addCookie(HttpServletResponse response, String name, String value, int maxAge) {
    ResponseCookie cookie = ResponseCookie.from(name, value)
        .path("/")
        .sameSite("LAX")
        .httpOnly(true)
        .secure(true)
        .maxAge(maxAge)
        .build();

    response.addHeader("Set-Cookie", cookie.toString());
  }
}

