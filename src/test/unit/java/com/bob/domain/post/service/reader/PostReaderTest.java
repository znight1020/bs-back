package com.bob.domain.post.service.reader;

import static com.bob.support.fixture.domain.PostFixture.DEFAULT_MOCK_POSTS;
import static com.bob.support.fixture.query.PostQueryFixture.defaultReadFilteredPostsQuery;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.bob.domain.post.entity.Post;
import com.bob.domain.post.repository.PostRepository;
import com.bob.domain.post.service.dto.query.ReadFilteredPostsQuery;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

@DisplayName("PostReader 테스트")
@ExtendWith(MockitoExtension.class)
class PostReaderTest {

  @InjectMocks
  private PostReader postReader;

  @Mock
  private PostRepository postRepository;

  private Pageable pageable = PageRequest.of(0, 10);

  @Test
  @DisplayName("게시글 목록 조회 테스트")
  void readFilteredPosts_success() {
    // given
    ReadFilteredPostsQuery query = defaultReadFilteredPostsQuery();
    List<Post> postsMock = DEFAULT_MOCK_POSTS();
    when(postRepository.findFilteredPosts(query, pageable)).thenReturn(postsMock);

    // when
    List<Post> result = postReader.readFilteredPosts(query, pageable);

    // then
    assertThat(result).hasSize(2);
    assertThat(result.get(0).getBook().getTitle()).isEqualTo("객체지향의 사실과 오해");
    assertThat(result.get(1).getBook().getTitle()).isEqualTo("오브젝트");
    verify(postRepository, times(1)).findFilteredPosts(query, pageable);
  }
}