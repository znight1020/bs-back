package com.bob.domain.post.repository;

import com.bob.domain.post.entity.Post;
import java.util.List;
import org.springframework.data.repository.CrudRepository;

public interface PostRepository extends CrudRepository<Post, Long> {

  List<Post> findAllBySellerId(Long sellerId);
}
