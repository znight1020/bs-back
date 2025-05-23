package com.bob.domain.post.adapter;

import com.bob.domain.member.service.port.PostSearcher;
import com.bob.domain.post.dto.PostResponse;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class PostProvider implements PostSearcher {

  @Override
  public List<PostResponse> readPostsOfMember(Long memberId) {
    // TODO : 사용자의 게시글 조회
    return List.of();
  }
}
