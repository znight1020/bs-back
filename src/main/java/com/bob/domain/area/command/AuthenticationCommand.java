package com.bob.domain.area.command;

import com.bob.domain.member.service.dto.command.AuthenticationPurpose;
import java.util.UUID;

public record AuthenticationCommand(
    Integer emdId,
    Double lat,
    Double lon,
    AuthenticationPurpose purpose,
    UUID memberId
) {

  public boolean isGuest() {
    return memberId == null;
  }
}
