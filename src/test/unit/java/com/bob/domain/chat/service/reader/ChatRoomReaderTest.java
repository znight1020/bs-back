package com.bob.domain.chat.service.reader;

import static com.bob.support.fixture.command.CreateChatRoomMembersCommandFixture.CHAT_ROOM_ID;
import static com.bob.support.fixture.domain.MemberFixture.MEMBER_ID;
import static com.bob.support.fixture.domain.MemberFixture.OTHER_MEMBER_ID;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

import com.bob.domain.chat.repository.ChatRoomRepository;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@DisplayName("채팅방 Reader 테스트")
@ExtendWith(MockitoExtension.class)
class ChatRoomReaderTest {

  @InjectMocks
  private ChatRoomReader chatRoomReader;

  @Mock
  private ChatRoomRepository chatRoomRepository;

  @Test
  @DisplayName("채팅방 ID 조회 테스트")
  void 동일한_게시글_판매자_구매자의_경우_이미_존재하는_채팅방_ID를_반환한다() {
    // given
    Long postId = 1L;
    UUID sellerId = MEMBER_ID;
    UUID buyerId = OTHER_MEMBER_ID;

    given(chatRoomRepository.findExistingChatRoom(postId, sellerId, buyerId)).willReturn(Optional.of(CHAT_ROOM_ID));

    // when
    Optional<Long> result = chatRoomReader.readExistingChatRoom(postId, sellerId, buyerId);

    // then
    assertThat(result).isPresent();
    assertThat(result.get()).isEqualTo(CHAT_ROOM_ID);
    then(chatRoomRepository).should().findExistingChatRoom(postId, sellerId, buyerId);
  }
}