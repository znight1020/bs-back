package com.bob.domain.post.service.dto.query.condition;

import java.util.Optional;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum SearchPrice {
  UNDER_5000(5_000),
  UNDER_10000(10_000),
  UNDER_20000(20_000),
  ALL(1_000_000);

  private final int maxPrice;

  public static Optional<SearchPrice> fromIndex(Integer index) {
    if (index == null || index < 0 || index >= values().length) {
      return Optional.empty();
    }

    return Optional.of(values()[index]);
  }
}