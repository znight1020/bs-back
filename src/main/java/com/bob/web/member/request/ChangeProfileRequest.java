package com.bob.web.member.request;

import com.bob.domain.member.service.dto.command.ChangeProfileCommand;

public record ChangeProfileRequest(
    String nickname
) {

  public ChangeProfileCommand toCommand(Long memberId) {
    return new ChangeProfileCommand(memberId, nickname);
  }
}
