package com.bob.domain.post.adapter;

import static com.bob.support.fixture.domain.MemberFixture.MEMBER_ID;
import static com.bob.support.fixture.query.PostQueryFixture.defaultReadMemberFavoritePostsQuery;
import static com.bob.support.fixture.response.PostResponseFixture.DEFAULT_FAVORITE_RESPONSE;
import static com.bob.support.fixture.response.PostResponseFixture.DEFAULT_POSTS_RESPONSE;
import static com.bob.support.fixture.response.PostResponseFixture.DEFAULT_POST_DETAIL_RESPONSE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;

import com.bob.domain.chat.service.dto.response.ChatPostResponse;
import com.bob.domain.post.service.PostService;
import com.bob.domain.post.service.dto.query.ReadFilteredPostsQuery;
import com.bob.domain.post.service.dto.query.ReadMemberFavoritePostsQuery;
import com.bob.domain.post.service.dto.response.PostDetailResponse;
import com.bob.domain.post.service.dto.response.PostFavoritesResponse;
import com.bob.domain.post.service.dto.response.PostsResponse;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

@DisplayName("게시글 제공자 테스트")
@ExtendWith(MockitoExtension.class)
class PostAdapterTest {

  @InjectMocks
  private PostAdapter postAdapter;

  @Mock
  private PostService postService;

  private Pageable pageable = PageRequest.of(0, 12);

  @Test
  @DisplayName("사용자 등록 게시글 조회 테스트")
  void 사용자_ID로_게시글_목록을_조회할_수_있다() {
    // given
    UUID memberId = MEMBER_ID;
    given(postService.readFilteredPostsProcess(ReadFilteredPostsQuery.of(memberId), pageable)).willReturn(DEFAULT_POSTS_RESPONSE());

    // when
    PostsResponse result = postAdapter.readMemberPostSummary(memberId, pageable);

    // then
    assertThat(result.totalCount()).isEqualTo(2);
    assertThat(result.posts().get(0).postTitle()).isEqualTo("객체지향의 사실과 오해");
    assertThat(result.posts().get(1).postTitle()).isEqualTo("오브젝트");
  }

  @Test
  @DisplayName("사용자 즐겨찾기 게시글 목록 조회 테스트")
  void 사용자_ID로_즐겨찾기_게시글_목록을_조회할_수_있다() {
    // given
    UUID memberId = MEMBER_ID;
    ReadMemberFavoritePostsQuery query = defaultReadMemberFavoritePostsQuery(memberId);
    given(postService.readMemberFavoritePostsProcess(query, pageable)).willReturn(DEFAULT_FAVORITE_RESPONSE());

    // when
    PostFavoritesResponse response = postAdapter.readMemberFavoritePostsSummary(memberId, pageable);

    // then
    assertThat(response.totalCount()).isEqualTo(2);
    assertThat(response.postFavorites().get(0).postTitle()).isEqualTo("객체지향의 사실과 오해");
    assertThat(response.postFavorites().get(1).postTitle()).isEqualTo("오브젝트");
  }

  @Test
  @DisplayName("채팅방 생성 시 사용하는 게시글 정보 조회 테스트")
  void postId로_게시글_요약_정보를_조회할_수_있다() {
    // given
    Long postId = 1L;
    PostDetailResponse expect = DEFAULT_POST_DETAIL_RESPONSE(postId);
    given(postService.readPostDetailProcess(any())).willReturn(expect);

    // when
    ChatPostResponse response = postAdapter.readChatPostSummary(postId);

    // then
    assertThat(response.postId()).isEqualTo(expect.postId());
    assertThat(response.sellerId()).isEqualTo(expect.sellerId());
    assertThat(response.title()).isEqualTo(expect.book().title());
    then(postService).should(times(1)).readPostDetailProcess(any());
  }
}