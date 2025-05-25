package com.bob.domain.member.entity;

import com.bob.domain.area.entity.activity.ActivityArea;
import com.bob.global.audit.BaseTime;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.util.Objects;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
@Entity
@Table(name = "members")
public class Member extends BaseTime {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(unique = true, length = 50, nullable = false)
  private String email;

  @Column(nullable = false)
  private String password;

  @Column(length = 20, nullable = false)
  private String nickname;

  @Column
  private String profileImageUrl;

  @OneToOne(
      mappedBy = "member",
      cascade = CascadeType.ALL,
      orphanRemoval = true,
      fetch = FetchType.LAZY
  )
  private ActivityArea activityArea;

  public void updateActivityArea(ActivityArea newActivityArea) {
    activityArea = newActivityArea;
  }

  public void updatePassword(String newPassword) {
    password = newPassword;
  }

  public void updateNickname(String newNickname) {
    nickname = newNickname;
  }

  public void updateProfileImageUrl(String newProfileImageUrl) {
    profileImageUrl = newProfileImageUrl;
  }

  public boolean isEqualsNickname(String oldNickname) {
    return Objects.equals(nickname, oldNickname);
  }
}
