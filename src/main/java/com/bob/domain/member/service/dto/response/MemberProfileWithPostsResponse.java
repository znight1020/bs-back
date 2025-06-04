package com.bob.domain.member.service.dto.response;

import com.bob.domain.member.service.dto.response.internal.MemberPostsResponse;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MemberProfileWithPostsResponse {

  MemberProfileResponse profile;
  MemberPostsResponse memberPosts;

  public static MemberProfileWithPostsResponse of(MemberProfileResponse profile, MemberPostsResponse memberPosts) {
    return MemberProfileWithPostsResponse.builder()
        .profile(profile)
        .memberPosts(memberPosts)
        .build();
  }
}
