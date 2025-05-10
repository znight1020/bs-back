package com.bob.domain.notification.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum NotificationType {
  TRADE("거래"),
  CHAT("채팅"),
  LIKE("찜");

  private final String type;
}
