package com.bob.domain.chat.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import com.bob.domain.chat.entity.type.ChatMessageType;
import com.bob.domain.member.entity.Member;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
@Entity
@Table(name = "chat_messages")
public class ChatMessage {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "chat_room_id", nullable = false)
  private ChatRoom chatRoom;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "sender_id", nullable = false)
  private Member sender;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private ChatMessageType chatMessageType;

  @Column(length = 500)
  private String chatMessage;

  @Column
  private String chatImageUrl;

  @Column(nullable = false)
  @Builder.Default
  private Boolean isRead = false;

  @Column(nullable = false)
  private LocalDateTime sentAt;
}
