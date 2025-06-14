package com.bob.domain.chat.repository;

import com.bob.domain.chat.entity.ChatRoomMember;
import java.util.List;
import org.springframework.data.repository.CrudRepository;

public interface ChatRoomMemberRepository extends CrudRepository<ChatRoomMember, Long> {

  List<ChatRoomMember> findByChatRoomId(Long chatRoomId);
}
