package com.bob.domain.post.repository;

import com.bob.domain.post.entity.PostFavorite;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.repository.CrudRepository;

public interface PostFavoriteRepository extends CrudRepository<PostFavorite, Long> {

  Optional<PostFavorite> findByMemberIdAndPostId(UUID memberId, Long postId);

  Boolean existsByMemberIdAndPostId(UUID memberId, Long postId);
}
