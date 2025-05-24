package com.bob.domain.post.service.dto.response;

import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class PostSummaryResponse {

  // NOTE : 추후 게시글 조회 기능 구현 시 변경 가능성
  Long postId;
  String postTitle;
  String tradeStatus;
  String thumbnailUrl;
  String bookStatus;
  Integer sellPrice;
  LocalDateTime createAt;
}
