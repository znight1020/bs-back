package com.bob.support.fixture.request;

public class CreateChatRoomRequestFixture {

  public static String DEFAULT_CREATE_CHAT_ROOM_REQUEST() {
    return """
        {
          "postId": 1,
          "buyerId": "123e4567-e89b-12d3-a456-426614174001"
        }
        """;
  }
}
