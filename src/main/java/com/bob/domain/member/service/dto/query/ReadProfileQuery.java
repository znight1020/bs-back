package com.bob.domain.member.service.dto.query;

public record ReadProfileQuery(
    Long memberId
) {

  public static ReadProfileQuery of(Long memberId) {
    return new ReadProfileQuery(memberId);
  }
}
