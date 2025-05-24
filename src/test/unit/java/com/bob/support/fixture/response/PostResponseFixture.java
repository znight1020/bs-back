package com.bob.support.fixture.response;

import com.bob.domain.post.service.dto.response.PostSummaryResponse;
import com.bob.domain.post.entity.status.BookStatus;
import com.bob.domain.trade.entity.status.TradeStatus;
import java.time.LocalDateTime;
import java.util.List;

public class PostResponseFixture {

  public static final PostSummaryResponse FIRST_POST = PostSummaryResponse.builder()
      .postId(10L)
      .postTitle("객체지향의 사실과 오해")
      .tradeStatus(TradeStatus.REQUESTED.getStatus())
      .sellPrice(10000)
      .thumbnailUrl("http://thumbnail1.com")
      .bookStatus(BookStatus.BEST.getStatus())
      .createAt(LocalDateTime.now())
      .build();

  public static final PostSummaryResponse SECOND_POST = PostSummaryResponse.builder()
      .postId(11L)
      .postTitle("오브젝트")
      .tradeStatus(TradeStatus.REQUESTED.getStatus())
      .sellPrice(15000)
      .thumbnailUrl("http://thumbnail2.com")
      .bookStatus(BookStatus.LOW.getStatus())
      .createAt(LocalDateTime.now())
      .build();

  public static List<PostSummaryResponse> DEFAULT_POST_SUMMARY() {
    return List.of(FIRST_POST, SECOND_POST);
  }
}
