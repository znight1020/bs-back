package com.bob.domain.post.service.dto.query;

import java.util.UUID;
import org.springframework.data.domain.Pageable;

public record ReadMemberPostsQuery(
    UUID memberId,
    Pageable pageable
) {

  public static ReadMemberPostsQuery of(UUID memberId, Pageable pageable) {
    return new ReadMemberPostsQuery(memberId, pageable);
  }
}
