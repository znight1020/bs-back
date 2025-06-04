package com.bob.domain.post.service;

import static com.bob.global.exception.response.ApplicationError.NOT_VERIFIED_MEMBER;
import static com.bob.support.fixture.command.CreatePostCommandFixture.defaultCreatePostCommand;
import static com.bob.support.fixture.domain.ActivityAreaFixture.defaultActivityArea;
import static com.bob.support.fixture.domain.BookFixture.defaultBook;
import static com.bob.support.fixture.domain.CategoryFixture.defaultCategory;
import static com.bob.support.fixture.domain.MemberFixture.authenticatedMember;
import static com.bob.support.fixture.domain.MemberFixture.customIdMember;
import static com.bob.support.fixture.domain.MemberFixture.defaultIdMember;
import static com.bob.support.fixture.domain.MemberFixture.unverifiedMember;
import static com.bob.support.fixture.domain.PostFixture.DEFAULT_MOCK_POSTS;
import static com.bob.support.fixture.domain.PostFixture.defaultPost;
import static com.bob.support.fixture.query.PostQueryFixture.defaultReadFilteredPostsQuery;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.BDDMockito.willDoNothing;
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
import com.bob.domain.post.service.dto.query.ReadFilteredPostsQuery;
import com.bob.domain.post.service.dto.query.ReadPostDetailQuery;
import com.bob.domain.post.service.dto.response.PostDetailResponse;
import com.bob.domain.post.service.dto.response.PostsResponse;
import com.bob.domain.post.service.reader.PostReader;
import com.bob.global.exception.exceptions.ApplicationException;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

@DisplayName("게시글 서비스 테스트")
@ExtendWith(MockitoExtension.class)
class PostServiceTest {

  @InjectMocks
  private PostService postService;

  @Mock
  private PostReader postReader;

  @Mock
  private BookService bookService;

  @Mock
  private MemberReader memberReader;

  @Mock
  private CategoryReader categoryReader;

  @Mock
  private PostRepository postRepository;

  Pageable pageable = PageRequest.of(0, 10);

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
    assertThat(saved.getBook()).isEqualTo(book);
    assertThat(saved.getSeller()).isEqualTo(member);
    assertThat(saved.getCategory()).isEqualTo(category);
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

  @DisplayName("게시글 목록 조회 테스트")
  @Test
  void 게시글_목록을_조회할_수_있다() {
    // given
    ReadFilteredPostsQuery query = defaultReadFilteredPostsQuery();
    given(postReader.readFilteredPosts(query, pageable)).willReturn(DEFAULT_MOCK_POSTS());
    given(postRepository.countFilteredPosts(query)).willReturn(2L);

    // when
    PostsResponse response = postService.readFilteredPostsProcess(query, pageable);

    // then
    assertThat(response.totalCount()).isEqualTo(2L);
    then(postReader).should(times(1)).readFilteredPosts(query, pageable);
    then(postRepository).should(times(1)).countFilteredPosts(query);
  }

  @DisplayName("게시글 상세 조회 - 작성자 본인")
  @Test
  void 게시글_작성자와_조회자가_같다면_isOwner는_true이다() {
    // given
    Member member = defaultIdMember();
    member.updateActivityArea(defaultActivityArea());
    Post post = defaultPost(defaultBook(), member, defaultCategory());
    ReadPostDetailQuery query = new ReadPostDetailQuery(member.getId(), post.getId());
    given(postReader.readPostById(post.getId())).willReturn(post);
    willDoNothing().given(postRepository).increaseViewCount(post.getId());

    // when
    PostDetailResponse response = postService.readPostDetailProcess(query);

    // then
    assertThat(response.isOwner()).isTrue();
    then(postRepository).should(times(1)).increaseViewCount(post.getId());
  }

  @DisplayName("게시글 상세 조회 - 작성자 본인 X")
  @Test
  void 게시글_작성자와_조회자가_다르면_isOwner는_false이다() {
    // given
    Member writer = defaultIdMember();
    writer.updateActivityArea(defaultActivityArea());
    Member otherUser = customIdMember(UUID.randomUUID());
    Post post = defaultPost(defaultBook(), writer, defaultCategory());
    ReadPostDetailQuery query = new ReadPostDetailQuery(otherUser.getId(), post.getId());
    given(postReader.readPostById(post.getId())).willReturn(post);
    willDoNothing().given(postRepository).increaseViewCount(post.getId());

    // when
    PostDetailResponse response = postService.readPostDetailProcess(query);

    // then
    assertThat(response.isOwner()).isFalse();
    then(postRepository).should(times(1)).increaseViewCount(post.getId());
  }
}
