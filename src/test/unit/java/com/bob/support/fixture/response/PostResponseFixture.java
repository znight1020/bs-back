package com.bob.support.fixture.response;

import com.bob.domain.post.service.dto.response.PostSummary;
import com.bob.domain.post.entity.status.BookStatus;
import com.bob.domain.trade.entity.status.TradeStatus;
import java.time.LocalDateTime;
import java.util.List;

public class PostResponseFixture {

  public static final PostSummary FIRST_POST = PostSummary.builder()
      .postId(1L)
      .postTitle("객체지향의 사실과 오해")
      .postStatus(TradeStatus.REQUESTED.getStatus())
      .sellPrice(10000)
      .thumbnailUrl("http://thumbnail1.com")
      .bookStatus(BookStatus.BEST.getStatus())
      .createAt(LocalDateTime.now())
      .build();

  public static final PostSummary SECOND_POST = PostSummary.builder()
      .postId(2L)
      .postTitle("오브젝트")
      .postStatus(TradeStatus.REQUESTED.getStatus())
      .sellPrice(15000)
      .thumbnailUrl("http://thumbnail2.com")
      .bookStatus(BookStatus.LOW.getStatus())
      .createAt(LocalDateTime.now())
      .build();

  public static List<PostSummary> DEFAULT_POST_SUMMARY() {
    return List.of(FIRST_POST, SECOND_POST);
  }
}
