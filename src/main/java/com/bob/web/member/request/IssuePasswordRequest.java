package com.bob.web.member.request;

import com.bob.domain.member.service.dto.command.IssuePasswordCommand;
import jakarta.validation.constraints.NotBlank;

public record IssuePasswordRequest(
    @NotBlank(message = "이메일은 필수입니다.")
    String email
) {

  public IssuePasswordCommand toCommand() {
    return new IssuePasswordCommand(email);
  }
}
