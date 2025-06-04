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
            .postId(summary.postId())
            .categoryId(summary.categoryId())
            .postTitle(summary.postTitle())
            .postStatus(summary.postStatus())
            .thumbnailUrl(summary.thumbnailUrl())
            .bookStatus(summary.bookStatus())
            .sellPrice(summary.sellPrice())
            .createdAt(summary.createdAt())
            .build()
        )
        .toList();
  }
}
