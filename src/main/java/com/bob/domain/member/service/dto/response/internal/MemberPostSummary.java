package com.bob.domain.member.service.dto.response.internal;

import com.bob.domain.post.service.dto.response.PostSummary;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MemberPostSummary {

  Long postId;
  Integer categoryId;
  String postTitle;
  String postStatus;
  String thumbnailUrl;
  String bookStatus;
  Integer sellPrice;
  LocalDateTime createdAt;

  public static List<MemberPostSummary> from(List<PostSummary> summaries) {
    return summaries.stream().map(summary -> MemberPostSummary.builder()
            .postId(summary.getPostId())
            .categoryId(summary.getCategoryId())
            .postTitle(summary.getPostTitle())
            .postStatus(summary.getPostStatus())
            .thumbnailUrl(summary.getThumbnailUrl())
            .bookStatus(summary.getBookStatus())
            .sellPrice(summary.getSellPrice())
            .createdAt(summary.getCreatedAt())
            .build()
        )
        .toList();
  }
}
