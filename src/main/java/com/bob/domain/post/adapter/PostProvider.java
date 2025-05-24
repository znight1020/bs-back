package com.bob.domain.post.adapter;

import com.bob.domain.member.service.port.PostSearcher;
import com.bob.domain.post.service.dto.response.PostSummaryResponse;
import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

@Component
public class PostProvider implements PostSearcher {

  @Override
  public List<PostSummaryResponse> readPostsOfMember(Long memberId, Pageable pageable) {
    // TODO : 사용자의 게시글 조회
    return List.of();
  }
}
