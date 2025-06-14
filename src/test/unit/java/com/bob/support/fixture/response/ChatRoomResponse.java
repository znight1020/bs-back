package com.bob.support.fixture.response;

import com.bob.domain.chat.service.dto.response.CreateChatRoomResponse;

public class ChatRoomResponse {

  public static final Long DEFAULT_CHATROOM_ID = 1L;

  public static CreateChatRoomResponse DEFAULT_CREATE_CHATROOM_RESPONSE() {
    return CreateChatRoomResponse.of(DEFAULT_CHATROOM_ID);
  }
}
