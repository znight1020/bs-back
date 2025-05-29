package com.bob.domain.area.service;

import com.bob.domain.area.command.AuthenticationCommand;
import com.bob.domain.area.entity.EmdArea;
import com.bob.domain.area.entity.activity.ActivityArea;
import com.bob.domain.area.entity.activity.ActivityAreaId;
import com.bob.domain.area.service.reader.AreaReader;
import com.bob.domain.member.entity.Member;
import com.bob.domain.member.service.reader.MemberReader;
import com.bob.global.exception.exceptions.ApplicationException;
import com.bob.global.exception.response.ApplicationError;
import com.bob.global.utils.geo.GeometryUtils;
import java.time.LocalDate;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.locationtech.jts.geom.Point;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional
@Service
public class AreaService {

  private final AreaReader areaReader;
  private final MemberReader memberReader;

  public void authenticate(AuthenticationCommand command) {
    EmdArea emdArea = areaReader.readEmdArea(command.emdId());
    Point currentUserPoint = GeometryUtils.createPoint(command.lat(), command.lon());

    if (!emdArea.getGeom().contains(currentUserPoint)) {
      throw new ApplicationException(ApplicationError.INVALID_AREA_AUTHENTICATION);
    }

    switch (command.purpose()) {
      case SIGN_UP -> {}
      case CHANGE_AREA -> handleChangeArea(command, emdArea);
      case RE_AUTHENTICATE -> handleReAuthenticate(command);
    }
  }

  private void handleChangeArea(AuthenticationCommand command, EmdArea emdArea) {
    verifyLoginMember(command.isGuest());
    Member member = memberReader.readMemberById(command.memberId());
    verifyIsSameArea(member.getActivityArea().getEmdArea().getId(), command.emdId());
    ActivityAreaId id = ActivityArea.createId(command.memberId(), command.emdId());
    member.updateActivityArea(ActivityArea.create(id, member, emdArea));
  }

  private void handleReAuthenticate(AuthenticationCommand command) {
    verifyLoginMember(command.isGuest());
    Member member = memberReader.readMemberById(command.memberId());
    member.getActivityArea().updateAuthenticationAt(LocalDate.now());
  }

  private void verifyLoginMember(boolean isGuest) {
    if (isGuest) {
      throw new ApplicationException(ApplicationError.NOT_EXISTS_MEMBER);
    }
  }

  private void verifyIsSameArea(Integer oldId, Integer newId) {
    if (Objects.equals(oldId, newId)) {
      throw new ApplicationException(ApplicationError.IS_SAME_REQUEST);
    }
  }
}
