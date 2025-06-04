package com.bob.web.post.request;

import com.bob.domain.post.service.dto.command.ChangePostCommand;
import com.bob.global.exception.exceptions.ApplicationException;
import com.bob.global.exception.response.ApplicationError;
import java.util.UUID;

public record ChangePostRequest(
    Integer sellPrice,
    String bookStatus,
    String description
) {
  public ChangePostCommand toCommand(Long postId, UUID memberId) {
    if (sellPrice == null && bookStatus == null && description == null) {
      throw new ApplicationException(ApplicationError.IS_SAME_REQUEST);
    }
    return new ChangePostCommand(postId, memberId, sellPrice, bookStatus, description);
  }
}
