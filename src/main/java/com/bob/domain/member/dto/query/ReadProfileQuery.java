package com.bob.domain.member.dto.query;

public record ReadProfileQuery(
    Long memberId
) {

  public static ReadProfileQuery of(Long memberId) {
    return new ReadProfileQuery(memberId);
  }
}
