package com.bob.domain.member.service.dto.query;

import java.util.UUID;

public record ReadProfileQuery(
    UUID memberId
) {

  public static ReadProfileQuery of(UUID memberId) {
    return new ReadProfileQuery(memberId);
  }
}
