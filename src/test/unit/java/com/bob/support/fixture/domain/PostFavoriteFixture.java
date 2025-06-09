package com.bob.support.fixture.domain;

import static com.bob.support.fixture.domain.BookFixture.defaultBook;
import static com.bob.support.fixture.domain.CategoryFixture.defaultCategory;
import static com.bob.support.fixture.domain.MemberFixture.authenticatedMember;
import static com.bob.support.fixture.domain.PostFixture.defaultPost;

import com.bob.domain.post.entity.PostFavorite;
import java.util.List;

public class PostFavoriteFixture {

  public static List<PostFavorite> DEFAULT_MOCK_POST_FAVORITES() {
    return List.of(
        PostFavorite.builder()
            .id(1L)
            .member(authenticatedMember())
            .post(defaultPost(defaultBook(), authenticatedMember(), defaultCategory()))
            .build(),
        PostFavorite.builder()
            .id(2L)
            .member(authenticatedMember())
            .post(defaultPost(defaultBook(), authenticatedMember(), defaultCategory()))
            .build()
    );
  }
}
