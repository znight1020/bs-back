package com.bob.domain.member.service.dto.response.internal;

import com.bob.domain.post.service.dto.response.PostSummaryResponse;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MemberPost {

  Long postId;
  String postTitle;
  String tradeStatus;
  String thumbnailUrl;
  String bookStatus;
  Integer sellPrice;
  LocalDateTime createAt;

  public static List<MemberPost> from(List<PostSummaryResponse> postResponses) {
    return postResponses.stream()
        .map(post -> MemberPost.builder()
            .postId(post.getPostId())
            .postTitle(post.getPostTitle())
            .tradeStatus(post.getTradeStatus())
            .thumbnailUrl(post.getThumbnailUrl())
            .bookStatus(post.getBookStatus())
            .sellPrice(post.getSellPrice())
            .createAt(post.getCreateAt())
            .build()
        )
        .toList();
  }
}
