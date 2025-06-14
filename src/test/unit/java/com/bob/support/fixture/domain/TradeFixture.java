package com.bob.support.fixture.domain;

import com.bob.domain.trade.entity.Trade;
import com.bob.domain.trade.entity.status.TradeStatus;
import com.bob.domain.trade.service.dto.command.CreateTradeCommand;
import java.time.LocalDateTime;

public class TradeFixture {

  public static Trade DEFAULT_ID_TRADE(CreateTradeCommand command) {
    return Trade.builder()
        .id(1L)
        .postId(command.postId())
        .sellerId(command.sellerId())
        .buyerId(command.buyerId())
        .tradeStatus(TradeStatus.REQUESTED)
        .updatedAt(LocalDateTime.now())
        .build();
  }
}
