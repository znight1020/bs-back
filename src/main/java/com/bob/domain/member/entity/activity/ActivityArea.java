package com.bob.domain.member.entity.activity;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import com.bob.domain.area.entity.EmdArea;
import com.bob.domain.member.entity.Member;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
@Entity
@Table(name = "activity_areas")
public class ActivityArea {

  @EmbeddedId
  private ActivityAreaId id;

  @MapsId("memberId")
  @OneToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "member_id")
  private Member member;

  @MapsId("emdAreaId")
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "emd_area_id")
  private EmdArea emdArea;
}
