package com.bob.domain.trade.service.dto.command;

import com.bob.domain.trade.entity.Trade;
import com.bob.domain.trade.entity.status.TradeStatus;
import java.time.LocalDateTime;
import java.util.UUID;

public record CreateTradeCommand(
    Long postId,
    UUID sellerId,
    UUID buyerId
) {

  public static CreateTradeCommand of(Long postId, UUID sellerId, UUID buyerId) {
    return new CreateTradeCommand(postId, sellerId, buyerId);
  }

  public Trade toTrade() {
    return Trade.builder()
        .postId(postId)
        .sellerId(sellerId)
        .buyerId(buyerId)
        .tradeStatus(TradeStatus.REQUESTED)
        .updatedAt(LocalDateTime.now())
        .build();
  }
}
