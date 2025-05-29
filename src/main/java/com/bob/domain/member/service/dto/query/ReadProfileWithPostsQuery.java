package com.bob.domain.member.service.dto.query;

import java.util.UUID;
import org.springframework.data.domain.Pageable;

public record ReadProfileWithPostsQuery(
    UUID memberId,
    Pageable pageable
) {

}
