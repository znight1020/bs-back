package com.bob.domain.post.service.helper;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.bob.global.exception.exceptions.ApplicationException;
import com.bob.global.exception.response.ApplicationError;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.dao.DataIntegrityViolationException;

@DisplayName("PostFavoriteHandler 테스트")
class PostFavoriteHandlerTest {

  @Test
  @DisplayName("safeRegister - 정상 실행 시 예외가 발생하지 않는다")
  void safeRegister_정상수행_예외없음() {
    // given
    Runnable action = () -> {};

    // when & then
    assertThatCode(() ->
        PostFavoriteHandler.safeRegister(action, ApplicationError.ALREADY_POST_FAVORITE)
    ).doesNotThrowAnyException();
  }

  @Test
  @DisplayName("safeRegister - 중복된 게시글 좋아요")
  void 중복예외_발생_시_DataIntegrityViolation_예외를_ApplicationException_변환() {
    // given
    Runnable action = () -> {
      throw new DataIntegrityViolationException("Duplicate entry");
    };

    // when & then
    assertThatThrownBy(() ->
        PostFavoriteHandler.safeRegister(action, ApplicationError.ALREADY_POST_FAVORITE)
    )
        .isInstanceOf(ApplicationException.class)
        .hasMessage(ApplicationError.ALREADY_POST_FAVORITE.getMessage());
  }
}