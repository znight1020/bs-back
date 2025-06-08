package com.bob.domain.post.service;

import static com.bob.global.exception.response.ApplicationError.ALREADY_POST_FAVORITE;
import static com.bob.support.fixture.domain.MemberFixture.defaultIdMember;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.BDDMockito.willThrow;
import static org.mockito.Mockito.times;

import com.bob.domain.member.entity.Member;
import com.bob.domain.post.entity.Post;
import com.bob.domain.post.entity.PostFavorite;
import com.bob.domain.post.repository.PostFavoriteRepository;
import com.bob.domain.post.service.reader.PostFavoriteReader;
import com.bob.global.exception.exceptions.ApplicationException;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;

@ExtendWith(MockitoExtension.class)
@DisplayName("PostFavoriteService 테스트")
class PostFavoriteServiceTest {

  @InjectMocks
  private PostFavoriteService postFavoriteService;

  @Mock
  private PostFavoriteRepository postFavoriteRepository;

  @Mock
  private PostFavoriteReader postFavoriteReader;

  @Mock
  private Post post;

  @Test
  @DisplayName("게시글 즐겨찾기 등록 - 성공 테스트")
  void 게시글_즐겨찾기_등록에_성공한다() {
    // given
    Member member = defaultIdMember();
    PostFavorite postFavorite = PostFavorite.create(member, post);
    given(postFavoriteRepository.save(any(PostFavorite.class))).willReturn(postFavorite);

    // when
    postFavoriteService.createPostFavoriteProcess(member, post);

    // then
    then(postFavoriteRepository).should().save(any(PostFavorite.class));
  }

  @Test
  @DisplayName("게시글 즐겨찾기 등록 - 실패 테스트 (중복 즐겨찾기)")
  void 이미_즐겨찾기한_게시글이면_예외가_발생한다() {
    // given
    Member member = defaultIdMember();
    willThrow(new DataIntegrityViolationException("duplicate"))
        .given(postFavoriteRepository)
        .save(any(PostFavorite.class));

    // when & then
    assertThatThrownBy(() ->
        postFavoriteService.createPostFavoriteProcess(member, post)
    )
        .isInstanceOf(ApplicationException.class)
        .hasMessage(ALREADY_POST_FAVORITE.getMessage());

    then(postFavoriteRepository).should().save(any(PostFavorite.class));
  }

  @Test
  @DisplayName("좋아요 해제 - 성공 테스트")
  void 좋아요를_성공적으로_해제한다() {
    // given
    Member member = defaultIdMember();
    PostFavorite postFavorite = PostFavorite.create(member, post);
    given(postFavoriteReader.readFavoritePostByMemberIdAndPostId(member.getId(), post.getId())).willReturn(postFavorite);

    // when
    postFavoriteService.deletePostFavoriteProcess(member.getId(), post.getId());

    // then
    then(postFavoriteReader)
        .should(times(1))
        .readFavoritePostByMemberIdAndPostId(member.getId(), post.getId());

    then(postFavoriteRepository)
        .should(times(1))
        .delete(postFavorite);
  }

  @Test
  @DisplayName("게시글 좋아요 여부 확인 - true 반환")
  void 게시글을_좋아요한_경우_true를_반환한다() {
    // given
    UUID memberId = UUID.randomUUID();
    Long postId = 1L;
    given(postFavoriteRepository.existsByMemberIdAndPostId(memberId, postId)).willReturn(true);

    // when
    boolean result = postFavoriteService.isFavorite(memberId, postId);

    // then
    assertThat(result).isTrue();
  }

  @Test
  @DisplayName("게시글 좋아요 여부 확인 - false 반환")
  void 게시글을_좋아요하지_않은_경우_false를_반환한다() {
    // given
    UUID memberId = UUID.randomUUID();
    Long postId = 2L;
    given(postFavoriteRepository.existsByMemberIdAndPostId(memberId, postId)).willReturn(false);

    // when
    boolean result = postFavoriteService.isFavorite(memberId, postId);

    // then
    assertThat(result).isFalse();
  }
}