package com.bob.support.fixture.response;

import static com.bob.support.fixture.domain.MemberFixture.MEMBER_ID;

import com.bob.domain.member.service.dto.response.MemberProfileResponse;

public class MemberProfileResponseFixture {

  public static final MemberProfileResponse DEFAULT_MEMBER_PROFILE_RESPONSE =
      MemberProfileResponse.builder()
          .memberId(MEMBER_ID)
          .nickname("tester")
          .profileImageUrl("http://image.url")
          .area(new MemberProfileResponse.Area(213, true))
          .build();
}
