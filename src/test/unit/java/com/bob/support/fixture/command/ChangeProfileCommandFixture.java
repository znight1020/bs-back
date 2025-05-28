package com.bob.support.fixture.command;

import com.bob.domain.member.service.dto.command.ChangeProfileCommand;
import java.util.UUID;

public class ChangeProfileCommandFixture {

  public static ChangeProfileCommand defaultChangeProfileCommand(UUID memberId) {
    return new ChangeProfileCommand(memberId, "new-nickname");
  }

  public static ChangeProfileCommand sameNicknameChangeProfileCommand(UUID memberId) {
    return new ChangeProfileCommand(memberId, "tester");
  }
}
