package com.bob.domain.post.service.dto.query.condition;

import java.util.Arrays;
import java.util.Optional;

public enum Sort {
  RECENT,
  OLD,
  LOW_PRICE,
  HIGH_PRICE;

  public static Optional<Sort> from(String value) {
    if (value == null) {
      return Optional.empty();
    }

    return Arrays.stream(values())
        .filter(sort -> sort.name().equalsIgnoreCase(value))
        .findFirst();
  }
}