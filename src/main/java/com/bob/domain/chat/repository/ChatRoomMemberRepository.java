package com.bob.domain.chat.repository;

import com.bob.domain.chat.entity.ChatRoomMember;
import org.springframework.data.repository.CrudRepository;

public interface ChatRoomMemberRepository extends CrudRepository<ChatRoomMember, Long> {

}
