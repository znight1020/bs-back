package com.bob.web.post.request;

import com.bob.domain.post.service.dto.command.CreatePostCommand;
import java.time.LocalDate;

public record CreatePostRequest(
    Long categoryId,
    Integer sellPrice,
    String bookStatus,
    String postDescription,
    BookInfo book
) {

  public CreatePostCommand toCommand(Long memberId) {
    return CreatePostCommand.builder()
        .memberId(memberId)
        .categoryId(categoryId)
        .sellPrice(sellPrice)
        .postDescription(postDescription)
        .bookStatus(bookStatus)
        .bookIsbn(book.isbn())
        .bookTitle(book.title())
        .bookAuthor(book.author())
        .bookDescription(book.description())
        .bookPriceStandard(book.priceStandard())
        .bookCover(book.cover())
        .bookPubDate(book.pubDate())
        .build();
  }
}

record BookInfo(
    String isbn,
    String title,
    String author,
    String description,
    Integer priceStandard,
    String cover,
    LocalDate pubDate
) {

}


