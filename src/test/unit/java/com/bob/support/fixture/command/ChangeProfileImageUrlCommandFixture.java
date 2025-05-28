package com.bob.support.fixture.command;

import static com.bob.support.fixture.domain.MemberFixture.MEMBER_ID;

import com.bob.domain.member.service.dto.command.ChangeProfileImageUrlCommand;
import java.util.UUID;

public class ChangeProfileImageUrlCommandFixture {

  public static ChangeProfileImageUrlCommand defaultChangeProfileImageUrlCommand() {
    return new ChangeProfileImageUrlCommand(MEMBER_ID, "image/png");
  }

  public static ChangeProfileImageUrlCommand customChangeProfileImageUrlCommand(UUID memberId) {
    return new ChangeProfileImageUrlCommand(memberId, "image/png");
  }

  public static ChangeProfileImageUrlCommand unSupportedChangeProfileImageUrlCommand() {
    return new ChangeProfileImageUrlCommand(MEMBER_ID, "video/mp4");
  }
}
