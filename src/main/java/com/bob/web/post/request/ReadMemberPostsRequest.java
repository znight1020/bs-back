package com.bob.web.post.request;

import com.bob.domain.post.service.dto.query.ReadMemberPostsQuery;
import java.util.UUID;
import org.springframework.data.domain.Pageable;

public record ReadMemberPostsRequest(

) {

  public static ReadMemberPostsQuery toQuery(UUID memberId, Pageable pageable) {
    return new ReadMemberPostsQuery(memberId, pageable);
  }
}
