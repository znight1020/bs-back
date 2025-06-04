package com.bob.support.fixture.response;

import static com.bob.support.fixture.response.MemberProfileResponseFixture.DEFAULT_MEMBER_PROFILE_RESPONSE;
import static com.bob.support.fixture.response.internal.MemberPostFixture.DEFAULT_MEMBER_POSTS;

import com.bob.domain.member.service.dto.response.MemberProfileWithPostsResponse;

public class MemberProfileWithPostsResponseFixture {

  public static final MemberProfileWithPostsResponse DEFAULT_MEMBER_PROFILE_WITH_POSTS_RESPONSE =
      MemberProfileWithPostsResponse.builder()
          .profile(DEFAULT_MEMBER_PROFILE_RESPONSE)
          .memberPosts(DEFAULT_MEMBER_POSTS())
          .build();
}
