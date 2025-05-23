package com.bob.support.fixture.command;

import com.bob.domain.area.command.AuthenticationCommand;
import com.bob.domain.member.dto.command.AuthenticationPurpose;

public class AuthenticationCommandFixture {

  public static final int DEFAULT_EMD_ID = 213;
  public static final double DEFAULT_LAT = 37.500578;
  public static final double DEFAULT_LON = 127.036991;
  public static final Long DEFAULT_MEMBER_ID = 1L;

  public static AuthenticationCommand defaultAuthenticationCommand() {
    return new AuthenticationCommand(
        DEFAULT_EMD_ID,
        DEFAULT_LAT,
        DEFAULT_LON,
        AuthenticationPurpose.SIGN_UP,
        DEFAULT_MEMBER_ID
    );
  }

  public static AuthenticationCommand defaultMismatchAuthenticationCommand() {
    return new AuthenticationCommand(
        DEFAULT_EMD_ID,
        DEFAULT_LAT+1,
        DEFAULT_LON+1,
        AuthenticationPurpose.SIGN_UP,
        DEFAULT_MEMBER_ID
    );
  }

  public static AuthenticationCommand defaultChangeAreaCommand() {
    return new AuthenticationCommand(
        DEFAULT_EMD_ID,
        DEFAULT_LAT,
        DEFAULT_LON,
        AuthenticationPurpose.CHANGE_AREA,
        DEFAULT_MEMBER_ID
    );
  }

  public static AuthenticationCommand defaultReAuthenticateCommand() {
    return new AuthenticationCommand(
        DEFAULT_EMD_ID,
        DEFAULT_LAT,
        DEFAULT_LON,
        AuthenticationPurpose.RE_AUTHENTICATE,
        DEFAULT_MEMBER_ID
    );
  }

  public static AuthenticationCommand customAuthenticationCommand(Long memberId, AuthenticationPurpose purpose) {
    return new AuthenticationCommand(
        DEFAULT_EMD_ID,
        DEFAULT_LAT,
        DEFAULT_LON,
        purpose,
        memberId
    );
  }

  public static AuthenticationCommand guestCommand(AuthenticationPurpose purpose) {
    return new AuthenticationCommand(
        DEFAULT_EMD_ID,
        DEFAULT_LAT,
        DEFAULT_LON,
        purpose,
        null
    );
  }
}