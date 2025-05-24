package com.bob.web.member.request;

import com.bob.domain.member.service.dto.command.CreateMemberCommand;

public record SignupRequest(
    String nickname,
    String email,
    String password,
    Integer emdId
) {

  public CreateMemberCommand toCommand() {
    return new CreateMemberCommand(email, password, nickname, emdId);
  }
}
