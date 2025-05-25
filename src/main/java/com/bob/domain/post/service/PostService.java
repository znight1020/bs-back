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
import com.bob.global.exception.exceptions.ApplicationException;
import com.bob.global.exception.response.ApplicationError;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class PostService {

  private final PostRepository postRepository;

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
    if(member.getActivityArea().isValidAuthentication()) return;
    throw new ApplicationException(ApplicationError.NOT_VERIFIED_MEMBER);
  }
}
