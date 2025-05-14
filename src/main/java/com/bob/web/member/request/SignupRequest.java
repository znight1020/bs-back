package com.bob.web.member.request;

import com.bob.domain.member.command.CreateMemberCommand;

public record SignupRequest(
    String nickname,
    String email,
    String password
) {

  public CreateMemberCommand toCommand() {
    return new CreateMemberCommand(email, password, nickname);
  }
}
