package com.bob.domain.post.adapter;

import static com.bob.support.fixture.domain.MemberFixture.MEMBER_ID;
import static com.bob.support.fixture.response.PostResponseFixture.DEFAULT_POSTS_RESPONSE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

import com.bob.domain.post.service.PostService;
import com.bob.domain.post.service.dto.query.ReadFilteredPostsQuery;
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
class PostProviderTest {

  @InjectMocks
  private PostProvider postProvider;

  @Mock
  private PostService postService;

  @Test
  @DisplayName("사용자 등록 게시글 조회 테스트")
  void 사용자_ID로_게시글_목록을_조회할_수_있다() {
    // given
    UUID memberId = MEMBER_ID;
    Pageable pageable = PageRequest.of(0, 10);
    given(postService.readFilteredPostsProcess(ReadFilteredPostsQuery.of(memberId), pageable)).willReturn(DEFAULT_POSTS_RESPONSE());

    // when
    PostsResponse result = postProvider.readMemberPostSummary(memberId, pageable);

    // then
    assertThat(result.totalCount()).isEqualTo(2);
    assertThat(result.posts().get(0).postTitle()).isEqualTo("객체지향의 사실과 오해");
    assertThat(result.posts().get(1).postTitle()).isEqualTo("오브젝트");
  }
}