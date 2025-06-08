package com.bob.domain.post.service.dto.query;

import java.util.UUID;

public record ReadMemberFavoritePostsQuery(
    UUID memberId
) {

  public static ReadMemberFavoritePostsQuery of(UUID memberId) {
    return new ReadMemberFavoritePostsQuery(memberId);
  }
}
