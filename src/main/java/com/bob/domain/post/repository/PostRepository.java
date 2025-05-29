package com.bob.domain.post.repository;

import com.bob.domain.post.entity.Post;
import java.util.List;
import java.util.UUID;
import org.springframework.data.repository.CrudRepository;

public interface PostRepository extends CrudRepository<Post, Long> {

  List<Post> findAllBySellerId(UUID sellerId);
}
