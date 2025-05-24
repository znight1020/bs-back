package com.bob.domain.member.service.dto.response;

import com.bob.domain.member.service.dto.response.internal.MemberPost;
import java.util.List;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MemberProfileWithPostsResponse {

  MemberProfileResponse profile;
  List<MemberPost> posts;

  public static MemberProfileWithPostsResponse of(MemberProfileResponse profile, List<MemberPost> posts) {
    return MemberProfileWithPostsResponse.builder()
        .profile(profile)
        .posts(posts)
        .build();
  }
}
