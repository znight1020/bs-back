package com.bob.domain.post.service.reader;

import static com.bob.global.exception.response.ApplicationError.INVALID_POST_FAVORITE;
import static com.bob.support.fixture.domain.MemberFixture.defaultIdMember;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

import com.bob.domain.member.entity.Member;
import com.bob.domain.post.entity.Post;
import com.bob.domain.post.entity.PostFavorite;
import com.bob.domain.post.repository.PostFavoriteRepository;
import com.bob.global.exception.exceptions.ApplicationException;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
@DisplayName("PostFavoriteReader 테스트")
class PostFavoriteReaderTest {

  @InjectMocks
  private PostFavoriteReader postFavoriteReader;

  @Mock
  private PostFavoriteRepository postFavoriteRepository;

  @Mock
  private Post post;

  @Test
  @DisplayName("좋아요 게시글 조회 - 성공 테스트")
  void 좋아요가_존재하면_PostFavorite를_반환한다() {
    // given
    Member member = defaultIdMember();
    PostFavorite postFavorite = PostFavorite.create(member, post);
    given(postFavoriteRepository.findByMemberIdAndPostId(member.getId(), post.getId())).willReturn(Optional.of(postFavorite));

    // when
    PostFavorite result = postFavoriteReader.readFavoritePostByMemberIdAndPostId(member.getId(), post.getId());

    // then
    assertNotNull(result);
    then(postFavoriteRepository).should().findByMemberIdAndPostId(member.getId(), post.getId());
  }

  @Test
  @DisplayName("좋아요 게시글 조회 - 실패 테스트(존재하지 않는 좋아요 게시글)")
  void 좋아요가_없으면_ApplicationException이_발생한다() {
    // given
    Member member = defaultIdMember();
    given(postFavoriteRepository.findByMemberIdAndPostId(member.getId(), post.getId())).willReturn(Optional.empty());

    // when & then
    assertThatThrownBy(() ->
        postFavoriteReader.readFavoritePostByMemberIdAndPostId(member.getId(), post.getId())
    )
        .isInstanceOf(ApplicationException.class)
        .hasMessage(INVALID_POST_FAVORITE.getMessage());
  }
}