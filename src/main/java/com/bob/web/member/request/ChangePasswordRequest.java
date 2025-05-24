package com.bob.web.member.request;

import com.bob.domain.member.service.dto.command.ChangePasswordCommand;

public record ChangePasswordRequest(
    String oldPassword,
    String newPassword
) {

  public ChangePasswordCommand toCommand(Long memberId) {
    return new ChangePasswordCommand(memberId, oldPassword, newPassword);
  }
}
