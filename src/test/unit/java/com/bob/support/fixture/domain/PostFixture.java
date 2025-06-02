package com.bob.support.fixture.domain;

import com.bob.domain.book.entity.Book;
import com.bob.domain.category.entity.Category;
import com.bob.domain.member.entity.Member;
import com.bob.domain.post.entity.Post;
import com.bob.domain.post.entity.status.BookStatus;
import com.bob.domain.post.entity.status.PostStatus;

public class PostFixture {

  public static Post defaultPost(Book book, Member seller, Category category) {
    return Post.builder()
        .book(book)
        .seller(seller)
        .category(category)
        .bookStatus(BookStatus.BEST)
        .postStatus(PostStatus.READY)
        .sellPrice(30000)
        .description("Description")
        .registrationAreaId(seller.getActivityArea().getId().getEmdAreaId())
        .thumbnailUrl("https://image/1.png")
        .build();
  }
}
