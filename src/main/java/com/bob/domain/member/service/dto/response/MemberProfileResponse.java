package com.bob.domain.member.service.dto.response;

import com.bob.domain.area.entity.activity.ActivityArea;
import com.bob.domain.member.entity.Member;
import java.util.UUID;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MemberProfileResponse {

  private UUID memberId;
  private String nickname;
  private String profileImageUrl;
  private Area area;

  public static MemberProfileResponse of(Member member) {
    ActivityArea aa = member.getActivityArea();
    return MemberProfileResponse.builder()
        .memberId(member.getId())
        .nickname(member.getNickname())
        .profileImageUrl(member.getProfileImageUrl())
        .area(Area.of(aa.getId().getEmdAreaId(), aa.isValidAuthentication()))
        .build();
  }

  public record Area(
      Integer emdId,
      Boolean isAuthentication
  ) {

    static Area of(Integer emdId, Boolean isAuthentication) {
      return new Area(emdId, isAuthentication);
    }
  }
}


