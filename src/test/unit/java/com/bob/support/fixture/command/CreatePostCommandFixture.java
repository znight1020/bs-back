package com.bob.support.fixture.command;

import com.bob.domain.post.service.dto.command.CreatePostCommand;
import java.time.LocalDate;

public class CreatePostCommandFixture {

  public static CreatePostCommand defaultCreatePostCommand() {
    return CreatePostCommand.builder()
        .memberId(1L)
        .categoryId(1L)
        .sellPrice(38000)
        .bookStatus("최상")
        .postDescription("거의 새 책입니다.")
        .bookIsbn("9788966264414")
        .bookTitle("JVM 밑바닥까지 파헤치기")
        .bookAuthor("저우즈밍")
        .bookDescription("JVM 핵심 원리 소개")
        .bookPriceStandard(43000)
        .bookCover("https://image.jpg")
        .bookPubDate(LocalDate.of(2024, 4, 29))
        .build();
  }
}
