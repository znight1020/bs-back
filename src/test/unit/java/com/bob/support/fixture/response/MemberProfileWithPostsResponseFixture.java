package com.bob.support.fixture.response;

import static com.bob.support.fixture.response.MemberProfileResponseFixture.DEFAULT_MEMBER_PROFILE_RESPONSE;
import static com.bob.support.fixture.response.PostResponseFixture.DEFAULT_POST_LIST;

import com.bob.domain.member.dto.response.MemberProfileWithPostsResponse;

public class MemberProfileWithPostsResponseFixture {

  public static final MemberProfileWithPostsResponse DEFAULT_MEMBER_PROFILE_WITH_POSTS_RESPONSE =
      MemberProfileWithPostsResponse.builder()
          .profile(DEFAULT_MEMBER_PROFILE_RESPONSE)
          .posts(DEFAULT_POST_LIST())
          .build();
}
