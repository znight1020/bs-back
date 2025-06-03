package com.bob.domain.post.service.dto.query;

import com.bob.domain.post.service.dto.query.condition.SearchKey;
import com.bob.domain.post.service.dto.query.condition.SearchPrice;
import com.bob.domain.post.service.dto.query.condition.SortKey;
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
    SortKey sortKey
) {

}
