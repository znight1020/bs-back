package com.bob.web.member.request;

import com.bob.domain.member.service.dto.command.ChangeProfileImageUrlCommand;

public record ReadImageUploadUrlRequest(
    String contentType
) {

  public ChangeProfileImageUrlCommand toQuery(Long memberId) {
    return new ChangeProfileImageUrlCommand(memberId, contentType);
  }
}
