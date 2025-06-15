package com.bob.domain.chat.service.port.out;

import com.bob.domain.chat.service.dto.response.ChatPostResponse;

public interface ChatPostPort {
  ChatPostResponse readChatPostSummary(Long postId);
}
