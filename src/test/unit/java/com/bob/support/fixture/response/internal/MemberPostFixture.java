package com.bob.support.fixture.response.internal;

import static com.bob.support.fixture.response.PostResponseFixture.FIRST_POST;
import static com.bob.support.fixture.response.PostResponseFixture.SECOND_POST;

import com.bob.domain.member.service.dto.response.internal.MemberPost;
import java.util.List;

public class MemberPostFixture {

  public static List<MemberPost> DEFAULT_POST_LIST() {
    return MemberPost.from(List.of(FIRST_POST, SECOND_POST));
  }
}
