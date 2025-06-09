package com.bob.domain.post.entity;

import static com.bob.support.fixture.domain.BookFixture.defaultBook;
import static com.bob.support.fixture.domain.CategoryFixture.defaultCategory;
import static com.bob.support.fixture.domain.MemberFixture.authenticatedMember;
import static com.bob.support.fixture.domain.PostFixture.defaultIdPost;
import static org.assertj.core.api.Assertions.assertThat;

import com.bob.domain.book.entity.Book;
import com.bob.domain.category.entity.Category;
import com.bob.domain.member.entity.Member;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("PostFavorite 도메인 테스트")
class PostFavoriteTest {

  @Test
  @DisplayName("PostFavorite 생성 - 정상 테스트")
  void PostFavorite를_정상적으로_생성할_수_있다() {
    // given
    Book book = defaultBook();
    Member member = authenticatedMember();
    Category category = defaultCategory();
    Post post = defaultIdPost(book, member, category);

    // when
    PostFavorite postFavorite = PostFavorite.create(member, post);

    // then
    assertThat(postFavorite).isNotNull();
    assertThat(postFavorite.getMember()).isEqualTo(member);
    assertThat(postFavorite.getPost()).isEqualTo(post);
  }
}
