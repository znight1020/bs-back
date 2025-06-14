package com.bob.domain.post.service.dto.response;

import com.bob.domain.post.entity.Post;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import lombok.Builder;

@Builder
public record PostDetailResponse(
    Long postId,
    UUID sellerId,
    Integer sellPrice,
    String bookStatus,
    String postStatus,
    Integer category,
    BookInfo book,
    String description,
    List<String> images,
    WriterInfo writer,
    Integer scrapCount,
    Integer viewCount,
    Boolean isFavorite,
    Boolean isOwner,
    LocalDateTime createdAt
) {

  public static PostDetailResponse of(Post post, boolean isFavorite, boolean isOwner, List<String> images) {
    return PostDetailResponse.builder()
        .postId(post.getId())
        .sellerId(post.getSeller().getId())
        .sellPrice(post.getSellPrice())
        .bookStatus(post.getBookStatus().name())
        .postStatus(post.getPostStatus().getStatus())
        .category(post.getCategory().getId())
        .book(BookInfo.of(post))
        .description(post.getDescription())
        .images(images)
        .writer(WriterInfo.of(post))
        .scrapCount(post.getScrapCount())
        .viewCount(post.getViewCount())
        .isFavorite(isFavorite)
        .isOwner(isOwner)
        .createdAt(post.getCreatedAt())
        .build();
  }

  @Builder
  public record BookInfo(
      String title,
      String author,
      String description,
      Integer priceStandard,
      String pubDate
  ) {

    public static BookInfo of(Post post) {
      return BookInfo.builder()
          .title(post.getBook().getTitle())
          .author(post.getBook().getAuthor())
          .description(post.getBook().getDescription())
          .priceStandard(post.getBook().getPriceStandard())
          .pubDate(post.getBook().getPubDate().toString())
          .build();
    }
  }

  @Builder
  public record WriterInfo(
      UUID memberId,
      String nickname,
      String activityArea,
      String profileUrl
  ) {

    public static WriterInfo of(Post post) {
      var activityArea = post.getSeller().getActivityArea();
      var emdArea = activityArea.getEmdArea();
      var siggArea = emdArea.getSiggArea();
      String activityAreaName = String.format("%s %s", siggArea.getName(), emdArea.getName());

      return WriterInfo.builder()
          .memberId(post.getSeller().getId())
          .nickname(post.getSeller().getNickname())
          .activityArea(activityAreaName)
          .profileUrl(post.getSeller().getProfileImageUrl())
          .build();
    }
  }
}