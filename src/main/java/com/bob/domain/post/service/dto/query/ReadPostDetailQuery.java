package com.bob.domain.post.service.dto.query;

import java.util.UUID;

public record ReadPostDetailQuery(
    UUID memberId,
    Long postId
) {

}
