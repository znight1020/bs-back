package com.bob.domain.post.service;

import com.bob.domain.book.entity.Book;
import com.bob.domain.book.service.BookService;
import com.bob.domain.category.entity.Category;
import com.bob.domain.category.service.reader.CategoryReader;
import com.bob.domain.member.entity.Member;
import com.bob.domain.member.service.reader.MemberReader;
import com.bob.domain.post.entity.Post;
import com.bob.domain.post.repository.PostRepository;
import com.bob.domain.post.service.dto.command.ChangePostCommand;
import com.bob.domain.post.service.dto.command.CreatePostCommand;
import com.bob.domain.post.service.dto.command.RegisterPostFavoriteCommand;
import com.bob.domain.post.service.dto.query.ReadFilteredPostsQuery;
import com.bob.domain.post.service.dto.query.ReadMemberFavoritePostsQuery;
import com.bob.domain.post.service.dto.query.ReadPostDetailQuery;
import com.bob.domain.post.service.dto.response.PostDetailResponse;
import com.bob.domain.post.service.dto.response.PostFavoritesResponse;
import com.bob.domain.post.service.dto.response.PostsResponse;
import com.bob.domain.post.service.reader.PostReader;
import com.bob.global.exception.exceptions.ApplicationException;
import com.bob.global.exception.response.ApplicationError;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class PostService {

  private final PostRepository postRepository;
  private final PostReader postReader;

  private final PostFavoriteService postFavoriteService;

  private final BookService bookService;
  private final MemberReader memberReader;
  private final CategoryReader categoryReader;

  @Transactional
  public void createPostProcess(CreatePostCommand command) {
    Book book = bookService.createBookProcess(command.toBookCreateCommand());
    Member member = memberReader.readMemberById(command.memberId());
    verifyAreaAuthentication(member);
    Category category = categoryReader.readCategoryById(command.categoryId());
    Post post = command.toPost(book, member, category);
    postRepository.save(post);
  }

  private void verifyAreaAuthentication(Member member) {
    if (member.getActivityArea().isValidAuthentication()) {
      return;
    }
    throw new ApplicationException(ApplicationError.NOT_VERIFIED_MEMBER);
  }

  @Transactional
  public void registerPostFavoriteProcess(RegisterPostFavoriteCommand command) {
    Member member = memberReader.readMemberById(command.memberId());
    Post post = postReader.readPostById(command.postId());
    postFavoriteService.createPostFavoriteProcess(member, post);
    postRepository.increaseFavoriteCount(post.getId());
  }

  @Transactional
  public void unregisterPostFavoriteProcess(RegisterPostFavoriteCommand command) {
    postFavoriteService.deletePostFavoriteProcess(command.memberId(), command.postId());
    postRepository.decreaseFavoriteCount(command.postId());
  }

  @Transactional(readOnly = true)
  public PostsResponse readFilteredPostsProcess(ReadFilteredPostsQuery query, Pageable pageable) {
    List<Post> posts = postReader.readFilteredPosts(query, pageable);
    Long totalCount = postRepository.countFilteredPosts(query);
    return PostsResponse.of(totalCount, posts);
  }

  @Transactional(readOnly = true)
  public PostFavoritesResponse readMemberFavoritePostsProcess(ReadMemberFavoritePostsQuery query, Pageable pageable) {
    return postFavoriteService.readMemberFavoritePosts(query.memberId(), pageable);
  }

  @Transactional
  public PostDetailResponse readPostDetailProcess(ReadPostDetailQuery query) {
    postRepository.increaseViewCount(query.postId());
    Post post = postReader.readPostById(query.postId());
    boolean isOwner = query.memberId() != null && post.getSeller().getId().equals(query.memberId());
    boolean isFavorite = postFavoriteService.isFavorite(query.memberId(), post.getId());
    return PostDetailResponse.of(post, isFavorite, isOwner, List.of()); // TODO : 첨부 이미지 기능 구현 시 이미지 경로 List 매핑
  }

  @Transactional
  public void changePostProcess(ChangePostCommand command) {
    Post post = postReader.readPostById(command.postId());
    verifyPostOwner(command.memberId(), post.getSeller().getId());
    post.updateOptionalFields(command.sellPrice(), command.bookStatus(), command.description());
  }

  private void verifyPostOwner(UUID requestMemberId, UUID postMemberId) {
    if (!Objects.equals(requestMemberId, postMemberId)) {
      throw new ApplicationException(ApplicationError.NOT_POST_OWNER);
    }
  }
}
