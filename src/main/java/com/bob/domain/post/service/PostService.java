package com.bob.domain.post.service;

import com.bob.domain.book.entity.Book;
import com.bob.domain.book.service.BookService;
import com.bob.domain.category.entity.Category;
import com.bob.domain.category.service.reader.CategoryReader;
import com.bob.domain.member.entity.Member;
import com.bob.domain.member.service.reader.MemberReader;
import com.bob.domain.post.entity.Post;
import com.bob.domain.post.repository.PostRepository;
import com.bob.domain.post.service.dto.command.CreatePostCommand;
import com.bob.domain.post.service.dto.query.ReadFilteredPostsQuery;
import com.bob.domain.post.service.dto.query.ReadPostDetailQuery;
import com.bob.domain.post.service.dto.response.PostDetailResponse;
import com.bob.domain.post.service.dto.response.PostsResponse;
import com.bob.domain.post.service.reader.PostReader;
import com.bob.global.exception.exceptions.ApplicationException;
import com.bob.global.exception.response.ApplicationError;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class PostService {

  private final PostRepository postRepository;
  private final PostReader postReader;

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

  @Transactional(readOnly = true)
  public PostsResponse readFilteredPostsProcess(ReadFilteredPostsQuery query, Pageable pageable) {
    List<Post> posts = postReader.readFilteredPosts(query, pageable);
    Long totalCount = postRepository.countFilteredPosts(query);
    return PostsResponse.of(totalCount, posts);
  }

  @Transactional
  public PostDetailResponse readPostDetailProcess(ReadPostDetailQuery query) {
    postRepository.increaseViewCount(query.postId());
    Post post = postReader.readPostById(query.postId());
    boolean isOwner = query.memberId() != null && post.getSeller().getId().equals(query.memberId());
    boolean isFavorite = false; // TODO : 게시글 좋아요 기능 구현 후 좋아요 여부 매핑
    return PostDetailResponse.of(post, isFavorite, isOwner, List.of()); // TODO : 첨부 이미지 기능 구현 시 이미지 경로 List 매핑
  }
}
