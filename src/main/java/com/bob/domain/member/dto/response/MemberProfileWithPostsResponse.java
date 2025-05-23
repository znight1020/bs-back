package com.bob.domain.member.dto.response;

import com.bob.domain.post.dto.PostResponse;
import java.util.List;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MemberProfileWithPostsResponse {

  MemberProfileResponse profile;
  List<PostResponse> posts;

  public static MemberProfileWithPostsResponse of(MemberProfileResponse profile, List<PostResponse> posts) {
    return MemberProfileWithPostsResponse.builder()
        .profile(profile)
        .posts(posts)
        .build();
  }
}
