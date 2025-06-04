package com.bob.domain.post.entity;

import static com.bob.support.fixture.domain.ActivityAreaFixture.defaultActivityArea;
import static com.bob.support.fixture.domain.BookFixture.defaultBook;
import static com.bob.support.fixture.domain.CategoryFixture.defaultCategory;
import static com.bob.support.fixture.domain.MemberFixture.defaultMember;
import static com.bob.support.fixture.domain.PostFixture.defaultPost;
import static org.assertj.core.api.Assertions.assertThat;

import com.bob.domain.member.entity.Member;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
@DisplayName("Post 도메인 테스트")
class PostTest {

  private Member member;

  @BeforeEach
  void setUp() {
    member = defaultMember();
    member.updateActivityArea(defaultActivityArea());
  }

  @Test
  @DisplayName("일부 필드만 null이 아닐 때 해당 필드만 업데이트된다")
  void 일부_필드만_업데이트된다() {
    // given
    Post post = defaultPost(defaultBook(), member, defaultCategory());

    Integer newSellPrice = 8000;
    String newBookStatus = null;
    String newDescription = "커피 자국 있음";

    // when
    post.updateOptionalFields(newSellPrice, newBookStatus, newDescription);

    // then
    assertThat(post.getSellPrice()).isEqualTo(newSellPrice);
    assertThat(post.getDescription()).isEqualTo(newDescription);
  }

  @Test
  @DisplayName("모든 필드가 null이면 아무 것도 변경되지 않는다")
  void 모든_필드가_null이면_변경되지_않는다() {
    // given
    Post post = defaultPost(defaultBook(), member, defaultCategory());

    Integer beforePrice = post.getSellPrice();
    String beforeStatus = post.getBookStatus().getStatus();
    String beforeDescription = post.getDescription();

    // when
    post.updateOptionalFields(null, null, null);

    // then
    assertThat(post.getSellPrice()).isEqualTo(beforePrice);
    assertThat(post.getBookStatus().getStatus()).isEqualTo(beforeStatus);
    assertThat(post.getDescription()).isEqualTo(beforeDescription);
  }

  @Test
  @DisplayName("모든 필드가 주어지면 모두 업데이트된다")
  void 모든_필드가_업데이트된다() {
    // given
    Post post = defaultPost(defaultBook(), member, defaultCategory());

    Integer newSellPrice = 6000;
    String newBookStatus = "하";
    String newDescription = "찢어진 페이지 있음";

    // when
    post.updateOptionalFields(newSellPrice, newBookStatus, newDescription);

    // then
    assertThat(post.getSellPrice()).isEqualTo(newSellPrice);
    assertThat(post.getBookStatus().getStatus()).isEqualTo(newBookStatus);
    assertThat(post.getDescription()).isEqualTo(newDescription);
  }
}