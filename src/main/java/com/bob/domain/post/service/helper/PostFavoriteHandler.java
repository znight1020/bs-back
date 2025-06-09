package com.bob.domain.post.service.helper;

import com.bob.global.exception.exceptions.ApplicationException;
import com.bob.global.exception.response.ApplicationError;
import org.springframework.dao.DataIntegrityViolationException;

public class PostFavoriteHandler {

  public static void safeRegister(Runnable action, ApplicationError error) {
    try {
      action.run();
    } catch (DataIntegrityViolationException e) {
      throw new ApplicationException(error);
    }
  }
}
