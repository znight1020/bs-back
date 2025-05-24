package com.bob.support.fixture.response;

import com.bob.domain.member.service.dto.response.MemberProfileImageUrlResponse;

public class MemberProfileImageUrlResponseFixture {

  public static final MemberProfileImageUrlResponse DEFAULT_MEMBER_PROFILE_IMAGE_URL_RESPONSE =
      MemberProfileImageUrlResponse.builder()
          .fileName("profile/test.png")
          .imageUploadUrl("https://s3-url.com/presigned")
          .build();

  public static MemberProfileImageUrlResponse mockPresignedUrlResponse(String fileName) {
    String dummyUrl = "https://dummy-presigned-url.com/" + fileName;
    return MemberProfileImageUrlResponse.builder()
        .fileName(fileName)
        .imageUploadUrl(dummyUrl)
        .build();
  }
}
