package com.bob.domain.member.command;

import com.bob.domain.area.entity.EmdArea;
import com.bob.domain.member.entity.Member;
import com.bob.domain.member.entity.activity.ActivityArea;
import com.bob.domain.member.entity.activity.ActivityAreaId;

public record CreateMemberCommand(
    String email,
    String password,
    String nickname,
    Integer emdId
) {

  public Member toMember(EmdArea emdArea, String encodedPassword) {
    Member member = Member.builder()
        .email(email)
        .password(encodedPassword)
        .nickname(nickname)
        .build();

    ActivityArea activityArea = ActivityArea.builder()
        .id(new ActivityAreaId(null, emdArea.getId()))
        .member(member)
        .emdArea(emdArea)
        .build();

    member.updateActivityArea(activityArea);

    return member;
  }
}
