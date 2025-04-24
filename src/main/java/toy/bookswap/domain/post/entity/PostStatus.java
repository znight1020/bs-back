package toy.bookswap.domain.post.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum PostStatus {
  NEW("new"),
  RESERVED("reserved"),
  SOLDOUT("soldout");

  private final String status;
}
