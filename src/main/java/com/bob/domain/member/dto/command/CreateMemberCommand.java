package com.bob.domain.member.dto.command;

import com.bob.domain.area.entity.EmdArea;
import com.bob.domain.area.entity.activity.ActivityArea;
import com.bob.domain.area.entity.activity.ActivityAreaId;
import com.bob.domain.member.entity.Member;
import java.time.LocalDate;

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
        .authenticationAt(LocalDate.now())
        .build();

    member.updateActivityArea(activityArea);

    return member;
  }
}
