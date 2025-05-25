package com.bob.support.fixture.command;

import com.bob.domain.member.service.dto.command.ChangeProfileImageUrlCommand;

public class ChangeProfileImageUrlCommandFixture {

  public static ChangeProfileImageUrlCommand defaultChangeProfileImageUrlCommand() {
    return new ChangeProfileImageUrlCommand(1L, "image/png");
  }

  public static ChangeProfileImageUrlCommand customChangeProfileImageUrlCommand(Long memberId) {
    return new ChangeProfileImageUrlCommand(memberId, "image/png");
  }

  public static ChangeProfileImageUrlCommand unSupportedChangeProfileImageUrlCommand() {
    return new ChangeProfileImageUrlCommand(1L, "video/mp4");
  }
}
