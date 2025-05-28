package com.bob.web.member.request;

import com.bob.domain.member.service.dto.query.ReadProfileWithPostsQuery;
import java.util.UUID;
import org.springframework.data.domain.Pageable;

public record ReadProfileByIdRequest(
    UUID memberId
) {

  public static ReadProfileWithPostsQuery toQuery(UUID memberId, Pageable pageable) {
    return new ReadProfileWithPostsQuery(memberId, pageable);
  }
}
