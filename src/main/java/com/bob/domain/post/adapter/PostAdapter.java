package com.bob.domain.post.adapter;

import com.bob.domain.chat.service.dto.response.ChatPostResponse;
import com.bob.domain.chat.service.port.out.ChatPostPort;
import com.bob.domain.member.service.port.PostSearcher;
import com.bob.domain.post.service.PostService;
import com.bob.domain.post.service.dto.query.ReadFilteredPostsQuery;
import com.bob.domain.post.service.dto.query.ReadMemberFavoritePostsQuery;
import com.bob.domain.post.service.dto.query.ReadPostDetailQuery;
import com.bob.domain.post.service.dto.response.PostDetailResponse;
import com.bob.domain.post.service.dto.response.PostFavoritesResponse;
import com.bob.domain.post.service.dto.response.PostsResponse;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class PostAdapter implements PostSearcher, ChatPostPort {

  private final PostService postService;

  @Override
  public PostsResponse readMemberPostSummary(UUID memberId, Pageable pageable) {
    return postService.readFilteredPostsProcess(ReadFilteredPostsQuery.of(memberId), pageable);
  }

  @Override
  public PostFavoritesResponse readMemberFavoritePostsSummary(UUID memberId, Pageable pageable) {
    return postService.readMemberFavoritePostsProcess(ReadMemberFavoritePostsQuery.of(memberId), pageable);
  }

  @Override
  public ChatPostResponse readChatPostSummary(Long postId) {
    PostDetailResponse detail = postService.readPostDetailProcess(ReadPostDetailQuery.of(null, postId));
    return ChatPostResponse.of(detail.postId(), detail.sellerId(), detail.book().title());
  }
}
