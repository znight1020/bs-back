package com.bob.domain.area.command;

import com.bob.domain.member.dto.command.AuthenticationPurpose;

public record AuthenticationCommand(
    Integer emdId,
    Double lat,
    Double lon,
    AuthenticationPurpose purpose,
    Long memberId
) {

  public boolean isGuest() {
    return memberId == null;
  }
}
