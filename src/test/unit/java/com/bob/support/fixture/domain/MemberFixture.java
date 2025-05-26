package com.bob.support.fixture.domain;

import static com.bob.support.fixture.domain.ActivityAreaFixture.customTimeActivityArea;
import static com.bob.support.fixture.domain.ActivityAreaFixture.defaultActivityArea;
import static com.bob.support.fixture.domain.EmdAreaFixture.defaultEmdArea;

import com.bob.domain.member.entity.Member;
import java.time.LocalDate;

public class MemberFixture {

  public static Member defaultMember() {
    return Member.builder()
        .email("test@email.com")
        .password("password")
        .nickname("tester")
        .build();
  }

  public static Member defaultIdMember() {
    return Member.builder()
        .id(1L)
        .email("test@email.com")
        .password("password")
        .nickname("tester")
        .build();
  }

  public static Member otherMember() {
    return Member.builder()
        .email("unknown@email.com")
        .password("password")
        .nickname("anonymous")
        .build();
  }

  public static Member customEmailMember(String email) {
    return Member.builder()
        .email(email)
        .password("password")
        .nickname("tester")
        .build();
  }

  public static Member encryptPasswordMember(String encryptedPassword) {
    return Member.builder()
        .email("test@email.com")
        .password(encryptedPassword)
        .nickname("tester")
        .build();
  }

  public static Member authenticatedMember() {
    Member member = defaultMember();
    member.updateActivityArea(defaultActivityArea());
    return member;
  }

  public static Member unverifiedMember() {
    Member member = defaultMember();
    member.updateActivityArea(customTimeActivityArea(member, defaultEmdArea(), LocalDate.now().minusMonths(2)));
    return member;
  }
}
