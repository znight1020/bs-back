package com.bob.domain.trade.entity.status;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum TradeStatus {
  REQUESTED("거래 요청"),
  CANCELED("거래 취소"),
  COMPLETED("거래 완료");

  private final String status;
}
