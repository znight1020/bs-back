package com.bob.support.fixture.command;

import com.bob.domain.member.command.CreateMemberCommand;

public class CreateMemberCommandFixture {
  public static CreateMemberCommand defaultCreateMemberCommand() {
    return new CreateMemberCommand("test@email.com", "password", "tester", 1);
  }
}
