package com.bob.support.fixture.response;

import static com.bob.support.fixture.response.internal.MemberPostFixture.DEFAULT_MEMBER_POSTS;

import com.bob.domain.member.service.dto.response.internal.MemberPostsResponse;

public class MemberPostsResponseFixture {

  public static final MemberPostsResponse DEFAULT_MEMBER_POSTS_RESPONSE =
      MemberPostsResponse.builder()
          .totalCount(DEFAULT_MEMBER_POSTS().getTotalCount())
          .posts(DEFAULT_MEMBER_POSTS().getPosts())
          .build();
}
