package com.bob.support.fixture.response.internal;

import static com.bob.support.fixture.domain.PostFixture.DEFAULT_MOCK_POSTS;

import com.bob.domain.member.service.dto.response.internal.MemberPostsResponse;
import com.bob.domain.post.service.dto.response.PostsResponse;

public class MemberPostFixture {

  public static MemberPostsResponse DEFAULT_MEMBER_POSTS() {
    return MemberPostsResponse.from(PostsResponse.of(2L, DEFAULT_MOCK_POSTS()));
  }
}
