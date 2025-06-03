package com.bob.domain.post.service.dto.query.condition;

import java.util.Arrays;
import java.util.Optional;

public enum SortKey {
  RECENT,
  OLD,
  LOW_PRICE,
  HIGH_PRICE;

  public static Optional<SortKey> from(String value) {
    if (value == null) {
      return Optional.of(RECENT);
    }

    return Arrays.stream(values())
        .filter(key -> key.name().equalsIgnoreCase(value))
        .findFirst();
  }
}