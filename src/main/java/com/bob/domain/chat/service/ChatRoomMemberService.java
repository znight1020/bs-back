package com.bob.domain.chat.service;

import com.bob.domain.chat.entity.ChatRoomMember;
import com.bob.domain.chat.repository.ChatRoomMemberRepository;
import com.bob.domain.chat.service.dto.command.CreateChatRoomMembersCommand;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class ChatRoomMemberService {

  private final ChatRoomMemberRepository chatRoomMemberRepository;

  @Transactional
  public void registerChatRoomMembers(CreateChatRoomMembersCommand command) {
    List<ChatRoomMember> chatRoomMembers = command.toChatRoomMembers();
    chatRoomMemberRepository.saveAll(chatRoomMembers);
  }
}
