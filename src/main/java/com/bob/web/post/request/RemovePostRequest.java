package com.bob.web.post.request;

import com.bob.domain.post.service.dto.command.RemovePostCommand;
import java.util.UUID;

public record RemovePostRequest(

) {

  public static RemovePostCommand toCommand(UUID memberId, Long postId) {
    return new RemovePostCommand(memberId, postId);
  }
}
