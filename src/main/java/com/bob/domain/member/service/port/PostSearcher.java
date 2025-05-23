package com.bob.domain.member.service.port;

import com.bob.domain.post.dto.PostResponse;
import java.util.List;

public interface PostSearcher {

  List<PostResponse> readPostsOfMember(Long memberId);
}
