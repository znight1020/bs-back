package com.bob.domain.post.service.dto.query;

import static com.bob.domain.post.service.dto.query.condition.SortKey.RECENT;

import com.bob.domain.post.service.dto.query.condition.SearchKey;
import com.bob.domain.post.service.dto.query.condition.SearchPrice;
import com.bob.domain.post.service.dto.query.condition.SortKey;
import java.util.UUID;
import lombok.Builder;

@Builder
public record ReadFilteredPostsQuery(
    SearchKey key,
    String keyword,
    Integer emdId,
    Integer categoryId,
    SearchPrice price,
    String postStatus,
    String bookStatus,
    UUID memberId,
    SortKey sortKey
) {

  public static ReadFilteredPostsQuery of(UUID memberId) {
    return ReadFilteredPostsQuery.builder()
        .memberId(memberId)
        .sortKey(RECENT)
        .build();
  }
}
