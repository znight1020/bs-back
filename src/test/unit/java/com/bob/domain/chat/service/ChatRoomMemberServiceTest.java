package com.bob.domain.chat.service;

import static com.bob.support.fixture.command.CreateChatRoomMembersCommandFixture.DEFAULT_CREATE_CHAT_ROOM_MEMBERS_COMMAND;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;

import com.bob.domain.chat.entity.ChatRoomMember;
import com.bob.domain.chat.repository.ChatRoomMemberRepository;
import com.bob.domain.chat.service.dto.command.CreateChatRoomMembersCommand;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@DisplayName("채팅방 회원 서비스 테스트")
@ExtendWith(MockitoExtension.class)
class ChatRoomMemberServiceTest {

  @InjectMocks
  private ChatRoomMemberService chatRoomMemberService;

  @Mock
  private ChatRoomMemberRepository chatRoomMemberRepository;

  @Test
  @DisplayName("채팅방 회원 등록 테스트")
  void 채팅방에_회원을_등록할_수_있다() {
    // given
    CreateChatRoomMembersCommand command = DEFAULT_CREATE_CHAT_ROOM_MEMBERS_COMMAND();
    ArgumentCaptor<List<ChatRoomMember>> captor = ArgumentCaptor.forClass(List.class);

    // when
    chatRoomMemberService.registerChatRoomMembers(command);

    // then
    then(chatRoomMemberRepository).should(times(1)).saveAll(captor.capture());

    List<ChatRoomMember> savedMembers = captor.getValue();
    assertThat(savedMembers).hasSize(2);
    assertThat(savedMembers).allSatisfy(member ->
        assertThat(command.memberIds()).contains(member.getMemberId())
    );
  }
}
