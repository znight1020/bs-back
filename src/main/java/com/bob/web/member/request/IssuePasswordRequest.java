package com.bob.web.member.request;

import com.bob.domain.member.command.IssuePasswordCommand;

public record IssuePasswordRequest(
    String email
) {

  public IssuePasswordCommand toCommand() {
    return new IssuePasswordCommand(email);
  }
}
