package com.bob.domain.post.entity.status;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum BookStatus {
  BEST("최상"),
  HIGH("상"),
  MEDIUM("중"),
  LOW("하");

  private final String status;
}
