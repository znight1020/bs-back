package com.bob.domain.member.service.dto.response.internal;

import com.bob.domain.post.service.dto.response.PostsResponse;
import java.util.List;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MemberPostsResponse {

  Long totalCount;
  List<MemberPostSummary> posts;

  public static MemberPostsResponse from(PostsResponse summary) {
    return MemberPostsResponse.builder()
        .totalCount(summary.totalCount())
        .posts(MemberPostSummary.from(summary.posts()))
        .build();
  }
}
