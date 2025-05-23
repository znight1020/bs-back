package com.bob.support.fixture.response;

import com.bob.domain.member.dto.response.MemberProfileResponse;

public class MemberProfileResponseFixture {

  public static final MemberProfileResponse DEFAULT_MEMBER_PROFILE_RESPONSE =
      MemberProfileResponse.builder()
          .memberId(1L)
          .nickname("tester")
          .profileImageUrl("http://image.url")
          .area(new MemberProfileResponse.Area(213, true))
          .build();
}
