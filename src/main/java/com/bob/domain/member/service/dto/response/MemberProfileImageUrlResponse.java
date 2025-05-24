package com.bob.domain.member.service.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MemberProfileImageUrlResponse {

  private String fileName;
  private String imageUploadUrl;

  public static MemberProfileImageUrlResponse of(String fileName, String imageUploadUrl) {
    return MemberProfileImageUrlResponse.builder()
        .fileName(fileName)
        .imageUploadUrl(imageUploadUrl)
        .build();
  }
}
