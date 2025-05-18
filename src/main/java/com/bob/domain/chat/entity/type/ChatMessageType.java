package com.bob.domain.chat.entity.type;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ChatMessageType {
  MESSAGE("메시지"),
  IMAGE("사진"),
  SYSTEM("시스템");

  private final String messageType;
}
