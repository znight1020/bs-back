package com.bob.support.fixture.command;

import static com.bob.support.fixture.domain.MemberFixture.MEMBER_ID;
import static com.bob.support.fixture.domain.MemberFixture.OTHER_MEMBER_ID;

import com.bob.domain.chat.service.dto.command.CreateChatRoomMembersCommand;
import java.util.List;

public class CreateChatRoomMembersCommandFixture {

  public static final Long CHAT_ROOM_ID = 1L;

  public static CreateChatRoomMembersCommand DEFAULT_CREATE_CHAT_ROOM_MEMBERS_COMMAND() {
    return CreateChatRoomMembersCommand.of(CHAT_ROOM_ID, List.of(MEMBER_ID, OTHER_MEMBER_ID));
  }
}
