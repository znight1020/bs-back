package com.bob.domain.post.service.reader;

import static com.bob.global.exception.response.ApplicationError.NOT_EXIST_POST;
import static com.bob.support.fixture.domain.PostFixture.DEFAULT_MOCK_POSTS;
import static com.bob.support.fixture.query.PostQueryFixture.defaultReadFilteredPostsQuery;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

import com.bob.domain.post.entity.Post;
import com.bob.domain.post.repository.PostRepository;
import com.bob.domain.post.service.dto.query.ReadFilteredPostsQuery;
import com.bob.global.exception.exceptions.ApplicationException;
import java.util.List;
import java.util.Optional;
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

  private final Pageable pageable = PageRequest.of(0, 10);
  @InjectMocks
  private PostReader postReader;
  @Mock
  private PostRepository postRepository;

  @Test
  @DisplayName("게시글 목록 조회 테스트")
  void readFilteredPosts_success() {
    // given
    ReadFilteredPostsQuery query = defaultReadFilteredPostsQuery();
    List<Post> postsMock = DEFAULT_MOCK_POSTS();
    given(postRepository.findFilteredPosts(query, pageable)).willReturn(postsMock);

    // when
    List<Post> result = postReader.readFilteredPosts(query, pageable);

    // then
    assertThat(result).hasSize(2);
    assertThat(result.get(0).getBook().getTitle()).isEqualTo("객체지향의 사실과 오해");
    assertThat(result.get(1).getBook().getTitle()).isEqualTo("오브젝트");
    then(postRepository).should().findFilteredPosts(query, pageable);
  }

  @Test
  @DisplayName("게시글 ID로 조회 - 성공 테스트")
  void readPostById_success() {
    // given
    Long postId = 1L;
    Post post = DEFAULT_MOCK_POSTS().get(0);
    given(postRepository.findById(postId)).willReturn(Optional.of(post));

    // when
    Post result = postReader.readPostById(postId);

    // then
    assertThat(result).isEqualTo(post);
    then(postRepository).should().findById(postId);
  }

  @Test
  @DisplayName("게시글 ID로 조회 - 실패 테스트(존재하지 않는 ID)")
  void readPostById_fail() {
    // given
    Long invalidId = 999L;
    given(postRepository.findById(invalidId)).willReturn(Optional.empty());

    // when & then
    assertThatThrownBy(() -> postReader.readPostById(invalidId))
        .isInstanceOf(ApplicationException.class)
        .hasMessage(NOT_EXIST_POST.getMessage());

    then(postRepository).should().findById(invalidId);
  }
}