package com.bob.domain.post.service.dto.command;

import com.bob.domain.book.entity.Book;
import com.bob.domain.book.service.dto.BookCreateCommand;
import com.bob.domain.category.entity.Category;
import com.bob.domain.member.entity.Member;
import com.bob.domain.post.entity.Post;
import com.bob.domain.post.entity.status.BookStatus;
import com.bob.domain.post.entity.status.PostStatus;
import java.time.LocalDate;
import java.util.UUID;
import lombok.Builder;

@Builder
public record CreatePostCommand(
    UUID memberId,
    Integer categoryId,
    Integer sellPrice,
    String postDescription,
    String bookStatus,
    String bookIsbn,
    String bookTitle,
    String bookAuthor,
    String bookDescription,
    Integer bookPriceStandard,
    String bookCover,
    LocalDate bookPubDate
) {

  public Post toPost(Book book, Member member, Category category) {
    return Post.builder()
        .book(book)
        .seller(member)
        .category(category)
        .bookStatus(BookStatus.from(bookStatus))
        .postStatus(PostStatus.READY)
        .sellPrice(sellPrice)
        .description(postDescription)
        .registrationAreaId(member.getActivityArea().getId().getEmdAreaId())
        .thumbnailUrl(bookCover)
        .build();
  }

  public BookCreateCommand toBookCreateCommand() {
    return new BookCreateCommand(
        bookIsbn,
        bookTitle,
        bookAuthor,
        bookDescription,
        bookPriceStandard,
        bookCover,
        bookPubDate
    );
  }
}
