package com.bob.support.fixture.command;

import com.bob.domain.member.command.CreateMemberCommand;
import com.bob.domain.member.command.IssuePasswordCommand;

public class MemberCommandFixture {
  public static CreateMemberCommand defaultCreateMemberCommand() {
    return new CreateMemberCommand("test@email.com", "password", "tester", 213);
  }

  public static IssuePasswordCommand defaultIssuePasswordCommand() {
    return new IssuePasswordCommand("test@email.com");
  }
}
