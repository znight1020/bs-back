package com.bob.domain.post.repository;

import com.bob.domain.post.entity.PostFavorite;
import org.springframework.data.repository.CrudRepository;

public interface PostFavoriteRepository extends CrudRepository<PostFavorite, Long> {

}
