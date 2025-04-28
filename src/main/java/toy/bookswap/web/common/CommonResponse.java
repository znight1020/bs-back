package toy.bookswap.web.common;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CommonResponse<T> {

  private boolean success;
  private T result;
}
