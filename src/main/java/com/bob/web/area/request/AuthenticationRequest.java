package com.bob.web.area.request;

import com.bob.domain.area.command.AuthenticationCommand;
import com.bob.domain.member.dto.command.AuthenticationPurpose;

public record AuthenticationRequest(
    Integer emdId,
    Double lat,
    Double lon,
    String purpose
) {

  public AuthenticationCommand toCommand(Long memberId) {
    return new AuthenticationCommand(
        emdId,
        lat,
        lon,
        AuthenticationPurpose.valueOf(purpose),
        memberId
    );
  }
}
