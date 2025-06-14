package com.bob.domain.chat.repository;

import com.bob.domain.chat.entity.ChatRoom;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface ChatRoomRepository extends CrudRepository<ChatRoom, Long> {

  @Query("""
        SELECT crm.chatRoomId
        FROM ChatRoomMember crm
        WHERE crm.chatRoomId IN (
            SELECT cr.id
            FROM ChatRoom cr
            WHERE cr.postId = :postId
        )
        AND crm.memberId IN (:sellerId, :buyerId)
        GROUP BY crm.chatRoomId
        HAVING COUNT(DISTINCT crm.memberId) = 2
      """)
  Optional<Long> findExistingChatRoom(Long postId, UUID sellerId, UUID buyerId);
}
