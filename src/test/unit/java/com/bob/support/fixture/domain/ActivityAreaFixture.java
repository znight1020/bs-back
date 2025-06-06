package com.bob.support.fixture.domain;

import static com.bob.support.fixture.domain.MemberFixture.MEMBER_ID;

import com.bob.domain.area.entity.EmdArea;
import com.bob.domain.area.entity.activity.ActivityArea;
import com.bob.domain.area.entity.activity.ActivityAreaId;
import com.bob.domain.member.entity.Member;
import java.time.LocalDate;

public class ActivityAreaFixture {

  public static ActivityAreaId defaultActivityAreaId() {
    return new ActivityAreaId(MEMBER_ID, 213);
  }

  public static ActivityArea defaultActivityArea() {
    Member member = MemberFixture.defaultIdMember();
    EmdArea emdArea = EmdAreaFixture.defaultEmdArea();

    return ActivityArea.builder()
        .id(new ActivityAreaId(member.getId(), emdArea.getId()))
        .member(member)
        .emdArea(emdArea)
        .authenticationAt(LocalDate.now())
        .build();
  }

  public static ActivityArea customActivityArea(Member member, EmdArea emdArea) {
    return ActivityArea.builder()
        .id(new ActivityAreaId(member.getId(), emdArea.getId()))
        .member(member)
        .emdArea(emdArea)
        .authenticationAt(LocalDate.now())
        .build();
  }

  public static ActivityArea customTimeActivityArea(Member member, EmdArea emdArea, LocalDate date) {
    return ActivityArea.builder()
        .id(new ActivityAreaId(member.getId(), emdArea.getId()))
        .member(member)
        .emdArea(emdArea)
        .authenticationAt(date)
        .build();
  }
}