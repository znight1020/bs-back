package com.bob.support.fixture.domain;

import com.bob.domain.book.entity.Book;
import java.time.LocalDate;

public class BookFixture {
  public static final String DEFAULT_ISBN = "9788998139766";

  public static Book defaultBook() {
    return Book.builder()
        .isbn13(DEFAULT_ISBN)
        .title("객체지향의 사실과 오해")
        .description("설명")
        .priceStandard(10000)
        .cover("https://image.url")
        .pubDate(LocalDate.now())
        .build();
  }

  public static Book customBook(String isbn) {
    return Book.builder()
        .isbn13(isbn)
        .title("커스텀 책")
        .description("기본 설명")
        .priceStandard(12000)
        .cover("https://image.url/cover")
        .pubDate(LocalDate.now())
        .build();
  }
}
