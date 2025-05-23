package com.bob.web.member.request;

import com.bob.domain.member.dto.query.ReadProfileWithPostsQuery;
import org.springframework.data.domain.Pageable;

public record ReadProfileByIdRequest(
    Long memberId
) {

  public static ReadProfileWithPostsQuery toQuery(Long memberId, Pageable pageable) {
    return new ReadProfileWithPostsQuery(memberId, pageable);
  }
}
