package com.bob.domain.post.dto;

import com.bob.domain.post.entity.status.BookStatus;
import com.bob.domain.trade.entity.status.TradeStatus;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class PostResponse {

  // NOTE : 추후 게시글 조회 기능 구현 시 변경 가능성
  Long postId;
  String postTitle;
  TradeStatus tradeStatus;
  Integer sellPrice;
  String thumbnailUrl;
  BookStatus bookStatus;
  LocalDateTime createAt;
}
