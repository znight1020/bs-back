package com.bob.support.fixture.domain;

import com.bob.domain.member.entity.Member;

public class MemberFixture {

  public static Member defaultMember() {
    return Member.builder()
        .email("test@email.com")
        .password("encrypted-password")
        .nickname("테스터")
        .build();
  }

  public static Member defaultIdMember() {
    return Member.builder()
        .id(1L)
        .email("test@email.com")
        .password("encrypted-password")
        .nickname("테스터")
        .build();
  }

  public static Member otherMember() {
    return Member.builder()
        .email("unknown@email.com")
        .password("encrypted-password")
        .nickname("익명")
        .build();
  }

  public static Member customEmailMember(String email) {
    return Member.builder()
        .email(email)
        .password("encrypted-password")
        .nickname("테스터")
        .build();
  }
}
