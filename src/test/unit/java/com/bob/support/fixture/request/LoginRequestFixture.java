package com.bob.support.fixture.request;

import com.bob.infra.auth.filter.request.LoginRequest;

public class LoginRequestFixture {

  public static final String DEFAULT_EMAIL = "test@email.com";
  public static final String DEFAULT_PASSWORD = "1234";

  public static LoginRequest defaultLoginRequest() {
    return new LoginRequest(DEFAULT_EMAIL, DEFAULT_PASSWORD);
  }
}