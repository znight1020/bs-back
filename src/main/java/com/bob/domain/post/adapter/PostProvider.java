package com.bob.domain.post.adapter;

import com.bob.domain.member.service.port.PostSearcher;
import com.bob.domain.post.service.PostService;
import com.bob.domain.post.service.dto.query.ReadFilteredPostsQuery;
import com.bob.domain.post.service.dto.response.PostsResponse;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class PostProvider implements PostSearcher {

  private final PostService postService;

  @Override
  public PostsResponse readMemberPostSummary(UUID memberId, Pageable pageable) {
    return postService.readFilteredPostsProcess(ReadFilteredPostsQuery.of(memberId), pageable);
  }
}
