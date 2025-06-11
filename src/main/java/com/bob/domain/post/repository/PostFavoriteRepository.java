package com.bob.domain.post.repository;

import com.bob.domain.post.entity.PostFavorite;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;

public interface PostFavoriteRepository extends CrudRepository<PostFavorite, Long> {

  Optional<PostFavorite> findByMemberIdAndPostId(UUID memberId, Long postId);

  List<PostFavorite> findByMemberIdOrderByCreatedAtDesc(UUID memberId, Pageable pageable);

  Long countByMemberId(UUID memberId);

  Boolean existsByMemberIdAndPostId(UUID memberId, Long postId);

  void deleteAllByPostId(Long postId);
}
