package com.bob.domain.post.service.dto.query;

import java.util.UUID;

public record ReadPostDetailQuery(
    UUID memberId,
    Long postId
) {

  public static ReadPostDetailQuery of(UUID memberId, Long postId) {
    return new ReadPostDetailQuery(memberId, postId);
  }
}
