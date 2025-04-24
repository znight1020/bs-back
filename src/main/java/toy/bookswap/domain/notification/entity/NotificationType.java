package toy.bookswap.domain.notification.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum NotificationType {
  CHAT("chat"),
  LIKE("like");

  private final String type;
}
