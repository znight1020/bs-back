package com.bob.domain.member.dto.query;

import org.springframework.data.domain.Pageable;

public record ReadProfileWithPostsQuery(
    Long memberId,
    Pageable pageable
) {

}
