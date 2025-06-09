package com.bob.web.post.request;

import com.bob.domain.post.service.dto.command.RegisterPostFavoriteCommand;
import java.util.UUID;

public record RegisterPostFavoriteRequest(

) {

  public static RegisterPostFavoriteCommand toCommand(UUID memberId, Long postId) {
    return new RegisterPostFavoriteCommand(memberId, postId);
  }
}
