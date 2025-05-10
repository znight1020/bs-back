package com.bob.domain.post.entity.status;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum PostStatus {
  READY("거래 대기"),
  IN_PROGRESS("거래 진행"),
  COMPLETED("거래 완료");

  private final String status;
}
