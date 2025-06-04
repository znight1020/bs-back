package com.bob.support.fixture.query;

import com.bob.domain.post.service.dto.query.ReadFilteredPostsQuery;
import com.bob.domain.post.service.dto.query.condition.SearchKey;
import com.bob.domain.post.service.dto.query.condition.SearchPrice;
import com.bob.domain.post.service.dto.query.condition.SortKey;

public class PostQueryFixture {

  public static ReadFilteredPostsQuery defaultReadFilteredPostsQuery() {
    return ReadFilteredPostsQuery.builder()
        .key(SearchKey.ALL)
        .keyword(null)
        .emdId(null)
        .categoryId(null)
        .price(null)
        .postStatus(null)
        .bookStatus(null)
        .sortKey(SortKey.RECENT)
        .build();
  }

  public static ReadFilteredPostsQuery searchTitleQuery() {
    return ReadFilteredPostsQuery.builder()
        .key(SearchKey.TITLE)
        .keyword("오브젝트")
        .sortKey(SortKey.RECENT)
        .build();
  }

  public static ReadFilteredPostsQuery searchAuthorQuery() {
    return ReadFilteredPostsQuery.builder()
        .key(SearchKey.AUTHOR)
        .keyword("김영한")
        .sortKey(SortKey.RECENT)
        .build();
  }

  public static ReadFilteredPostsQuery searchUnderPriceQuery() {
    return ReadFilteredPostsQuery.builder()
        .price(SearchPrice.UNDER_5000)
        .sortKey(SortKey.RECENT)
        .build();
  }

  public static ReadFilteredPostsQuery searchCategoryQuery() {
    return ReadFilteredPostsQuery.builder()
        .categoryId(1)
        .sortKey(SortKey.RECENT)
        .build();
  }

  public static ReadFilteredPostsQuery searchTradeStatusQuery() {
    return ReadFilteredPostsQuery.builder()
        .postStatus("COMPLETED")
        .sortKey(SortKey.RECENT)
        .build();
  }

  public static ReadFilteredPostsQuery searchBookStatusQuery() {
    return ReadFilteredPostsQuery.builder()
        .bookStatus("BEST")
        .sortKey(SortKey.RECENT)
        .build();
  }

  public static ReadFilteredPostsQuery searchNewestQuery() {
    return ReadFilteredPostsQuery.builder()
        .sortKey(SortKey.RECENT)
        .build();
  }

  public static ReadFilteredPostsQuery searchOldestQuery() {
    return ReadFilteredPostsQuery.builder()
        .sortKey(SortKey.OLD)
        .build();
  }

  public static ReadFilteredPostsQuery searchLowPriceQuery() {
    return ReadFilteredPostsQuery.builder()
        .sortKey(SortKey.LOW_PRICE)
        .build();
  }

  public static ReadFilteredPostsQuery searchHighPriceQuery() {
    return ReadFilteredPostsQuery.builder()
        .sortKey(SortKey.HIGH_PRICE)
        .build();
  }
}
