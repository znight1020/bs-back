package com.bob.domain.post.service;

import static com.bob.global.exception.response.ApplicationError.NOT_VERIFIED_MEMBER;
import static com.bob.support.fixture.command.CreatePostCommandFixture.defaultCreatePostCommand;
import static com.bob.support.fixture.domain.BookFixture.defaultBook;
import static com.bob.support.fixture.domain.CategoryFixture.defaultCategory;
import static com.bob.support.fixture.domain.MemberFixture.authenticatedMember;
import static com.bob.support.fixture.domain.MemberFixture.unverifiedMember;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;

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
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@DisplayName("게시글 서비스 테스트")
@ExtendWith(MockitoExtension.class)
class PostServiceTest {

  @InjectMocks
  private PostService postService;

  @Mock
  private BookService bookService;

  @Mock
  private MemberReader memberReader;

  @Mock
  private CategoryReader categoryReader;

  @Mock
  private PostRepository postRepository;

  @Test
  @DisplayName("게시글 등록 - 성공 테스트")
  void 게시글을_등록할_수_있다() {
    // given
    CreatePostCommand command = defaultCreatePostCommand();
    Book book = defaultBook();
    Member member = authenticatedMember();
    Category category = defaultCategory();
    given(bookService.createBookProcess(command.toBookCreateCommand())).willReturn(book);
    given(memberReader.readMemberById(command.memberId())).willReturn(member);
    given(categoryReader.readCategoryById(command.categoryId())).willReturn(category);

    ArgumentCaptor<Post> captor = ArgumentCaptor.forClass(Post.class);

    // when
    postService.createPostProcess(command);

    // then
    then(bookService).should().createBookProcess(command.toBookCreateCommand());
    then(memberReader).should().readMemberById(command.memberId());
    then(categoryReader).should().readCategoryById(command.categoryId());
    then(postRepository).should(times(1)).save(captor.capture());

    Post saved = captor.getValue();
    Assertions.assertThat(saved.getBook()).isEqualTo(book);
    Assertions.assertThat(saved.getSeller()).isEqualTo(member);
    Assertions.assertThat(saved.getCategory()).isEqualTo(category);
  }

  @Test
  @DisplayName("게시글 등록 - 실패 테스트(위치 인증 안된 사용자)")
  void 위치인증_되지_않은_사용자는_게시글을_등록할_수_없다() {
    // given
    CreatePostCommand command = defaultCreatePostCommand();
    Member member = unverifiedMember();

    given(bookService.createBookProcess(command.toBookCreateCommand())).willReturn(defaultBook());
    given(memberReader.readMemberById(command.memberId())).willReturn(member);

    // when & then
    assertThatThrownBy(() -> postService.createPostProcess(command))
        .isInstanceOf(ApplicationException.class)
        .hasMessage(NOT_VERIFIED_MEMBER.getMessage());
  }
}
