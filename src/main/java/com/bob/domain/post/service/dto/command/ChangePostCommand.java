package com.bob.domain.post.service.dto.command;

import java.util.UUID;

public record ChangePostCommand(
    Long postId,
    UUID memberId,
    Integer sellPrice,
    String bookStatus,
    String description
) {

}
