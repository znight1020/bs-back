package com.bob.domain.chat.service.reader;

import com.bob.domain.chat.entity.ChatRoom;
import com.bob.domain.chat.repository.ChatRoomRepository;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class ChatRoomReader {

  private final ChatRoomRepository chatRoomRepository;

  public Optional<Long> readExistingChatRoom(Long postId, UUID sellerId, UUID buyerId) {
    return chatRoomRepository.findExistingChatRoom(postId, sellerId, buyerId);
  }
}
