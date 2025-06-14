package com.bob.domain.chat.service.dto.query;

public record ReadPostSummaryQuery(
    Long postId
) {

  public static ReadPostSummaryQuery of(Long postId) {
    return new ReadPostSummaryQuery(postId);
  }
}
