package com.bob.domain.post.service.reader;

import com.bob.domain.post.entity.PostFavorite;
import com.bob.domain.post.repository.PostFavoriteRepository;
import com.bob.global.exception.exceptions.ApplicationException;
import com.bob.global.exception.response.ApplicationError;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class PostFavoriteReader {

  private final PostFavoriteRepository postFavoriteRepository;

  public PostFavorite readFavoritePostByMemberIdAndPostId(UUID memberId, Long postId) {
    return postFavoriteRepository.findByMemberIdAndPostId(memberId, postId)
        .orElseThrow(() -> new ApplicationException(ApplicationError.INVALID_POST_FAVORITE));
  }

  public List<PostFavorite> readFavoritePostsByMemberId(UUID memberId, Pageable pageable) {
    return postFavoriteRepository.findByMemberIdOrderByCreatedAtDesc(memberId, pageable);
  }
}
