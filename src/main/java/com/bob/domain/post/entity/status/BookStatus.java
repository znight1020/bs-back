package com.bob.domain.post.entity.status;

import static com.bob.global.exception.response.ApplicationError.UN_SUPPORTED_BOOK_STATUS;

import com.bob.global.exception.exceptions.ApplicationException;
import java.util.Arrays;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum BookStatus {
  BEST("최상"),
  HIGH("상"),
  MEDIUM("중"),
  LOW("하");

  private final String status;

  public static BookStatus from(String status) {
    return Arrays.stream(BookStatus.values())
        .filter(value -> value.getStatus().equals(status))
        .findFirst()
        .orElseThrow(() -> new ApplicationException(UN_SUPPORTED_BOOK_STATUS));
  }
}
