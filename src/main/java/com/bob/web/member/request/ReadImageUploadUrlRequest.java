package com.bob.web.member.request;

import com.bob.domain.member.service.dto.command.ChangeProfileImageUrlCommand;
import java.util.UUID;

public record ReadImageUploadUrlRequest(
    String contentType
) {

  public ChangeProfileImageUrlCommand toQuery(UUID memberId) {
    return new ChangeProfileImageUrlCommand(memberId, contentType);
  }
}
