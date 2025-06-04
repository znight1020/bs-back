package com.bob.web.post.request;

import com.bob.domain.post.service.dto.query.ReadFilteredPostsQuery;
import com.bob.domain.post.service.dto.query.condition.SearchKey;
import com.bob.domain.post.service.dto.query.condition.SearchPrice;
import com.bob.domain.post.service.dto.query.condition.SortKey;

public record ReadFilteredPostsRequest(
    String key,
    String keyword,
    Integer emdId,
    Integer categoryId,
    Integer price,
    String postStatus,
    String bookStatus,
    String sortKey
) {

  public ReadFilteredPostsQuery toQuery() {
    return ReadFilteredPostsQuery.builder()
        .key(SearchKey.from(key).orElse(SearchKey.ALL))
        .keyword(keyword)
        .emdId(emdId)
        .categoryId(categoryId)
        .price(SearchPrice.fromIndex(price).orElse(SearchPrice.ALL))
        .postStatus(postStatus)
        .bookStatus(bookStatus)
        .sortKey(SortKey.from(sortKey).orElse(SortKey.RECENT))
        .build();
  }
}