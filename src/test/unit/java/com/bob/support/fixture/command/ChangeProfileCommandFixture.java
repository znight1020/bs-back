package com.bob.support.fixture.command;

import com.bob.domain.member.service.dto.command.ChangeProfileCommand;

public class ChangeProfileCommandFixture {

  public static ChangeProfileCommand defaultChangeProfileCommand(Long memberId) {
    return new ChangeProfileCommand(memberId, "new-nickname");
  }

  public static ChangeProfileCommand sameNicknameChangeProfileCommand(Long memberId) {
    return new ChangeProfileCommand(memberId, "tester");
  }
}
