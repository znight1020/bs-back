package com.bob.domain.chat.service.dto.response;

import java.util.UUID;

public record ChatPostResponse(
    Long postId,
    UUID sellerId,
    String title
) {

  public static ChatPostResponse of(Long postId, UUID sellerId, String title) {
    return new ChatPostResponse(postId, sellerId, title);
  }
}
