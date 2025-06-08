package com.bob.domain.post.repository;

import com.bob.domain.post.entity.Post;
import com.bob.domain.post.repository.dsl.CustomPostRepository;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface PostRepository extends CrudRepository<Post, Long>, CustomPostRepository {

  List<Post> findAllBySellerId(UUID sellerId);

  @Modifying
  @Query("UPDATE Post p SET p.viewCount = p.viewCount + 1 WHERE p.id = :postId")
  void increaseViewCount(@Param("postId") Long postId);

  @Modifying
  @Query("UPDATE Post p SET p.scrapCount = p.scrapCount + 1 WHERE p.id = :postId")
  void increaseFavoriteCount(@Param("postId") Long postId);
}
