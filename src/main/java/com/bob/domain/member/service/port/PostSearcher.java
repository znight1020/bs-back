package com.bob.domain.member.service.port;

import com.bob.domain.post.service.dto.response.PostSummaryResponse;
import java.util.List;
import org.springframework.data.domain.Pageable;

public interface PostSearcher {

  List<PostSummaryResponse> readPostsOfMember(Long memberId, Pageable pageable);
}
