package com.bob.domain.post.service.dto.command;

import java.util.UUID;

public record RegisterPostFavoriteCommand(
    UUID memberId,
    Long postId
) {

}
