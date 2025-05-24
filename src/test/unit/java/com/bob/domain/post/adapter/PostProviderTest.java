package com.bob.domain.post.adapter;

import static org.assertj.core.api.Assertions.assertThat;

import com.bob.domain.post.service.dto.response.PostSummaryResponse;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

@DisplayName("게시글 제공자 테스트")
@ExtendWith(MockitoExtension.class)
class PostProviderTest {

  @InjectMocks
  private PostProvider postProvider;

  @Test
  @DisplayName("사용자 등록 게시글 조회 테스트")
  void 사용자_ID로_게시글_목록을_조회할_수_있다() {
    // given
    Long memberId = 1L;
    Pageable pageable = PageRequest.of(0, 10);

    // when
    List<PostSummaryResponse> result = postProvider.readPostsOfMember(memberId, pageable);

    // then
    assertThat(result).hasSize(0);
    // assertThat(result.get(0).getPostTitle()).isEqualTo("객체지향의 사실과 오해");
    // assertThat(result.get(1).getPostTitle()).isEqualTo("오브젝트");
  }
}