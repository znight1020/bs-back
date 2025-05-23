package com.bob.support.fixture.response;

import com.bob.domain.post.dto.PostResponse;
import com.bob.domain.post.entity.status.BookStatus;
import com.bob.domain.trade.entity.status.TradeStatus;
import java.time.LocalDateTime;
import java.util.List;

public class PostResponseFixture {

  public static final PostResponse FIRST_POST = PostResponse.builder()
      .postId(10L)
      .postTitle("객체지향의 사실과 오해")
      .tradeStatus(TradeStatus.REQUESTED)
      .sellPrice(10000)
      .thumbnailUrl("http://thumbnail1.com")
      .bookStatus(BookStatus.BEST)
      .createAt(LocalDateTime.now())
      .build();

  public static final PostResponse SECOND_POST = PostResponse.builder()
      .postId(11L)
      .postTitle("오브젝트")
      .tradeStatus(TradeStatus.REQUESTED)
      .sellPrice(15000)
      .thumbnailUrl("http://thumbnail2.com")
      .bookStatus(BookStatus.LOW)
      .createAt(LocalDateTime.now())
      .build();

  public static List<PostResponse> DEFAULT_POST_LIST() {
    return List.of(FIRST_POST, SECOND_POST);
  }
}
