package com.bob.domain.post.service;

import static com.bob.domain.post.entity.PostFavorite.create;
import static com.bob.domain.post.service.helper.PostFavoriteHandler.safeRegister;
import static com.bob.global.exception.response.ApplicationError.ALREADY_POST_FAVORITE;

import com.bob.domain.member.entity.Member;
import com.bob.domain.post.entity.Post;
import com.bob.domain.post.entity.PostFavorite;
import com.bob.domain.post.repository.PostFavoriteRepository;
import com.bob.domain.post.service.reader.PostFavoriteReader;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class PostFavoriteService {

  private final PostFavoriteRepository postFavoriteRepository;
  private final PostFavoriteReader postFavoriteReader;

  @Transactional
  public void createPostFavoriteProcess(Member member, Post post) {
    safeRegister(() -> postFavoriteRepository.save(create(member, post)), ALREADY_POST_FAVORITE);
  }

  @Transactional
  public void deletePostFavoriteProcess(UUID memberId, Long postId) {
    PostFavorite favorite = postFavoriteReader.readFavoritePostByMemberIdAndPostId(memberId, postId);
    postFavoriteRepository.delete(favorite);
  }

}
