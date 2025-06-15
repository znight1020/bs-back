package com.bob.domain.chat.service.dto.response;

public record CreateChatRoomResponse(
    Long chatRoomId
) {

  public static CreateChatRoomResponse of(Long chatRoomId) {
    return new CreateChatRoomResponse(chatRoomId);
  }
}
