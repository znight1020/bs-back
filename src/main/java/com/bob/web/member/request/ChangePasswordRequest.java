package com.bob.web.member.request;

import com.bob.domain.member.service.dto.command.ChangePasswordCommand;
import java.util.UUID;

public record ChangePasswordRequest(
    String oldPassword,
    String newPassword
) {

  public ChangePasswordCommand toCommand(UUID memberId) {
    return new ChangePasswordCommand(memberId, oldPassword, newPassword);
  }
}
