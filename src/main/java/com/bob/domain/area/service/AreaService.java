package com.bob.domain.area.service;

import com.bob.domain.area.command.AuthenticationCommand;
import com.bob.domain.area.entity.EmdArea;
import com.bob.domain.area.repository.ActivityAreaRepository;
import com.bob.domain.area.service.reader.ActivityAreaReader;
import com.bob.domain.area.service.reader.AreaReader;
import com.bob.domain.member.entity.Member;
import com.bob.domain.area.entity.activity.ActivityArea;
import com.bob.domain.area.entity.activity.ActivityAreaId;
import com.bob.domain.member.service.reader.MemberReader;
import com.bob.global.exception.exceptions.ApplicationException;
import com.bob.global.exception.response.ApplicationError;
import com.bob.global.utils.geo.GeometryUtils;
import java.time.LocalDate;
import lombok.RequiredArgsConstructor;
import org.locationtech.jts.geom.Point;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class AreaService {

  private final ActivityAreaRepository activityAreaRepository;
  private final ActivityAreaReader activityAreaReader;
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

  private void handleReAuthenticate(AuthenticationCommand command) {
    if(command.isGuest()) throw new ApplicationException(ApplicationError.NOT_EXISTS_MEMBER);
    ActivityAreaId id = ActivityArea.createId(command.memberId(), command.emdId());
    ActivityArea activityArea = activityAreaReader.readActivityArea(id);
    activityArea.updateAuthenticationAt(LocalDate.now());
  }

  private void handleChangeArea(AuthenticationCommand command, EmdArea emdArea) {
    if(command.isGuest()) throw new ApplicationException(ApplicationError.NOT_EXISTS_MEMBER);
    ActivityAreaId id = ActivityArea.createId(command.memberId(), command.emdId());
    activityAreaRepository.deleteById(id);

    Member member = memberReader.readMemberById(command.memberId());
    ActivityArea newActivityArea = ActivityArea.create(id, member, emdArea);

    activityAreaRepository.save(newActivityArea);
    member.updateActivityArea(newActivityArea);
  }
}
