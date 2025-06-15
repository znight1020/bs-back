package com.bob.domain.chat.service.port.out;

import java.util.UUID;

public interface ChatTradePort {

  Long createTrade(Long postId, UUID sellerId, UUID buyerId);
}
