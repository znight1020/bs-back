package com.bob.domain.chat.service;

import com.bob.domain.chat.entity.ChatRoom;
import com.bob.domain.chat.repository.ChatRoomRepository;
import com.bob.domain.chat.service.dto.command.CreateChatRoomCommand;
import com.bob.domain.chat.service.dto.command.CreateChatRoomMembersCommand;
import com.bob.domain.chat.service.dto.response.ChatPostResponse;
import com.bob.domain.chat.service.port.out.ChatPostPort;
import com.bob.domain.chat.service.port.out.ChatTradePort;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class ChatRoomService {

  private final ChatRoomRepository chatRoomRepository;
  private final ChatRoomMemberService chatRoomMemberService;
  private final ChatPostPort postPort;
  private final ChatTradePort tradePort;

  @Transactional
  public void createChatRoomProcess(CreateChatRoomCommand command) {
    ChatPostResponse postResponse = postPort.readChatPostSummary(command.postId());
    Long tradeId = tradePort.createTrade(postResponse.postId(), postResponse.sellerId(), command.buyerId());
    ChatRoom chatRoom = command.toChatRoom(tradeId, postResponse.title());
    chatRoomRepository.save(chatRoom);
    chatRoomMemberService.registerChatRoomMembers(CreateChatRoomMembersCommand.of(
        chatRoom.getId(),
        List.of(postResponse.sellerId(), command.buyerId())
    ));
  }
}
