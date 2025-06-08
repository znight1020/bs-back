package com.bob.support.fixture.command;

import static com.bob.support.fixture.domain.MemberFixture.MEMBER_ID;

import com.bob.domain.post.service.dto.command.RegisterPostFavoriteCommand;

public class RegisterPostFavoriteCommandFixture {

  public static RegisterPostFavoriteCommand defaultRegisterPostFavoriteCommand() {
    return new RegisterPostFavoriteCommand(MEMBER_ID, 1L);
  }
}
