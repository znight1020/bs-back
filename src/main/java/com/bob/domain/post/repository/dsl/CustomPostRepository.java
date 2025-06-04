package com.bob.domain.post.repository.dsl;

import com.bob.domain.post.entity.Post;
import com.bob.domain.post.service.dto.query.ReadFilteredPostsQuery;
import java.util.List;
import org.springframework.data.domain.Pageable;

public interface CustomPostRepository {

  List<Post> findFilteredPosts(ReadFilteredPostsQuery query, Pageable pageable);

  Long countFilteredPosts(ReadFilteredPostsQuery query);
}
