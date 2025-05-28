package com.bob.web.member.request;

import com.bob.domain.member.service.dto.command.ChangeProfileCommand;
import java.util.UUID;

public record ChangeProfileRequest(
    String nickname
) {

  public ChangeProfileCommand toCommand(UUID memberId) {
    return new ChangeProfileCommand(memberId, nickname);
  }
}
