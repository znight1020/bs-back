package com.bob.domain.member.service.dto.query;

import org.springframework.data.domain.Pageable;

public record ReadProfileWithPostsQuery(
    Long memberId,
    Pageable pageable
) {

}
