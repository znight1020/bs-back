package com.bob.domain.area.service.reader;

import com.bob.domain.area.entity.activity.ActivityArea;
import com.bob.domain.area.entity.activity.ActivityAreaId;
import com.bob.domain.area.repository.ActivityAreaRepository;
import com.bob.global.exception.exceptions.ApplicationException;
import com.bob.global.exception.response.ApplicationError;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class ActivityAreaReader {

  private final ActivityAreaRepository activityAreaRepository;

  public ActivityArea readActivityArea(ActivityAreaId id) {
    return activityAreaRepository.findById(id)
        .orElseThrow(() -> new ApplicationException(ApplicationError.NOT_EXISTS_ACTIVITY_AREA));
  }
}
