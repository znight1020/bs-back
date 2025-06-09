package com.bob.domain.post.service;

import static com.bob.global.exception.response.ApplicationError.ALREADY_POST_FAVORITE;
import static com.bob.global.exception.response.ApplicationError.INVALID_POST_FAVORITE;
import static com.bob.global.exception.response.ApplicationError.NOT_VERIFIED_MEMBER;
import static com.bob.support.fixture.command.ChangePostCommandFixture.DEFAULT_CHANGE_POST_COMMAND;
import static com.bob.support.fixture.command.CreatePostCommandFixture.defaultCreatePostCommand;
import static com.bob.support.fixture.command.RegisterPostFavoriteCommandFixture.defaultRegisterPostFavoriteCommand;
import static com.bob.support.fixture.domain.ActivityAreaFixture.defaultActivityArea;
import static com.bob.support.fixture.domain.BookFixture.defaultBook;
import static com.bob.support.fixture.domain.CategoryFixture.defaultCategory;
import static com.bob.support.fixture.domain.MemberFixture.authenticatedMember;
import static com.bob.support.fixture.domain.MemberFixture.customIdMember;
import static com.bob.support.fixture.domain.MemberFixture.defaultIdMember;
import static com.bob.support.fixture.domain.MemberFixture.unverifiedMember;
import static com.bob.support.fixture.domain.PostFixture.DEFAULT_MOCK_POSTS;
import static com.bob.support.fixture.domain.PostFixture.defaultIdPost;
import static com.bob.support.fixture.domain.PostFixture.defaultPost;
import static com.bob.support.fixture.query.PostQueryFixture.defaultReadFilteredPostsQuery;
import static com.bob.support.fixture.query.PostQueryFixture.defaultReadMemberFavoritePostsQuery;
import static com.bob.support.fixture.response.PostResponseFixture.DEFAULT_FAVORITE_RESPONSE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.BDDMockito.willThrow;
import static org.mockito.Mockito.times;

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
  private PostFavoriteService postFavoriteService;

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

  @DisplayName("게시글 좋아요 - 성공 테스트")
  @Test
  void 게시글을_좋아요하면_좋아요_count가_증가한다() {
    // given
    RegisterPostFavoriteCommand command = defaultRegisterPostFavoriteCommand();
    UUID memberId = command.memberId();
    Long postId = command.postId();

    Book book = defaultBook();
    Member member = authenticatedMember();
    Category category = defaultCategory();
    Post post = defaultIdPost(book, member, category);

    given(memberReader.readMemberById(memberId)).willReturn(member);
    given(postReader.readPostById(postId)).willReturn(post);

    // when
    postService.registerPostFavoriteProcess(command);

    // then
    then(memberReader).should().readMemberById(memberId);
    then(postReader).should().readPostById(postId);
    then(postFavoriteService).should().createPostFavoriteProcess(member, post);
    then(postRepository).should().increaseFavoriteCount(postId);
  }

  @DisplayName("게시글 좋아요 - 실패 테스트 (이미 좋아요한 게시글)")
  @Test
  void 이미_좋아요한_게시글이면_예외가_발생한다() {
    // given
    RegisterPostFavoriteCommand command = defaultRegisterPostFavoriteCommand();
    UUID memberId = command.memberId();
    Long postId = command.postId();

    Book book = defaultBook();
    Member member = authenticatedMember();
    Category category = defaultCategory();
    Post post = defaultIdPost(book, member, category);

    given(memberReader.readMemberById(memberId)).willReturn(member);
    given(postReader.readPostById(postId)).willReturn(post);
    willThrow(new ApplicationException(ALREADY_POST_FAVORITE))
        .given(postFavoriteService).createPostFavoriteProcess(member, post);

    // when & then
    assertThatThrownBy(() -> postService.registerPostFavoriteProcess(command))
        .isInstanceOf(ApplicationException.class)
        .hasMessage(ALREADY_POST_FAVORITE.getMessage());

    then(memberReader).should().readMemberById(memberId);
    then(postReader).should().readPostById(postId);
    then(postFavoriteService).should().createPostFavoriteProcess(member, post);
    then(postRepository).shouldHaveNoInteractions();
  }

  @DisplayName("게시글 좋아요 해제 - 성공 테스트")
  @Test
  void 게시글_좋아요_해제를_요청하면_scrapCount가_감소한다() {
    // given
    RegisterPostFavoriteCommand command = defaultRegisterPostFavoriteCommand();

    // when
    postService.unregisterPostFavoriteProcess(command);

    // then
    then(postFavoriteService)
        .should(times(1))
        .deletePostFavoriteProcess(command.memberId(), command.postId());

    then(postRepository)
        .should(times(1))
        .decreaseFavoriteCount(command.postId());
  }

  @DisplayName("게시글 좋아요 해제 - 실패 테스트 (좋아요하지 않은 게시글)")
  @Test
  void 좋아요하지_않은_게시글은_해제할_수_없다() {
    // given
    RegisterPostFavoriteCommand command = defaultRegisterPostFavoriteCommand();

    willThrow(new ApplicationException(INVALID_POST_FAVORITE))
        .given(postFavoriteService)
        .deletePostFavoriteProcess(command.memberId(), command.postId());

    // when & then
    assertThatThrownBy(() -> postService.unregisterPostFavoriteProcess(command))
        .isInstanceOf(ApplicationException.class)
        .hasMessageContaining(INVALID_POST_FAVORITE.getMessage());

    then(postFavoriteService)
        .should(times(1))
        .deletePostFavoriteProcess(command.memberId(), command.postId());

    then(postRepository)
        .shouldHaveNoInteractions();
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

  @Test
  @DisplayName("좋아요한 게시글 목록 조회 테스트")
  void 좋아요한_게시글_목록을_조회할_수_있다() {
    // given
    UUID memberId = UUID.randomUUID();
    ReadMemberFavoritePostsQuery query = defaultReadMemberFavoritePostsQuery(memberId);
    given(postFavoriteService.readMemberFavoritePosts(memberId, pageable)).willReturn(DEFAULT_FAVORITE_RESPONSE());

    // when
    PostFavoritesResponse response = postService.readMemberFavoritePostsProcess(query, pageable);

    // then
    assertThat(response.totalCount()).isEqualTo(2L);
    then(postFavoriteService).should(times(1)).readMemberFavoritePosts(query.memberId(), pageable);
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
    then(postFavoriteService).should(times(1)).isFavorite(member.getId(), post.getId());
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
    then(postFavoriteService).should(times(1)).isFavorite(otherUser.getId(), post.getId());
  }

  @DisplayName("게시글 수정 - 성공 테스트")
  @Test
  void 게시글_작성자는_게시글을_수정할_수_있다() {
    // given
    Member writer = defaultIdMember();
    writer.updateActivityArea(defaultActivityArea());
    Post post = defaultPost(defaultBook(), writer, defaultCategory());
    ChangePostCommand command = DEFAULT_CHANGE_POST_COMMAND(writer.getId(), post.getId());
    given(postReader.readPostById(post.getId())).willReturn(post);

    // when
    postService.changePostProcess(command);

    // then
    assertThat(post.getSellPrice()).isEqualTo(12000);
    assertThat(post.getDescription()).isEqualTo("상태 좋음");
    then(postReader).should().readPostById(post.getId());
  }

  @DisplayName("게시글 수정 - 실패 테스트 (작성자 X)")
  @Test
  void 작성자가_아닌_사람은_게시글을_수정할_수_없다() {
    // given
    Member writer = defaultIdMember();
    writer.updateActivityArea(defaultActivityArea());
    Post post = defaultPost(defaultBook(), writer, defaultCategory());
    Member otherUser = customIdMember(UUID.randomUUID());
    given(postReader.readPostById(post.getId())).willReturn(post);
    ChangePostCommand command = DEFAULT_CHANGE_POST_COMMAND(otherUser.getId(), post.getId());

    // when & then
    assertThatThrownBy(() -> postService.changePostProcess(command))
        .isInstanceOf(ApplicationException.class)
        .hasMessage(ApplicationError.NOT_POST_OWNER.getMessage());

    then(postReader).should().readPostById(post.getId());
  }
}
