package com.bob.domain.area.reader;

import com.bob.domain.area.entity.EmdArea;
import com.bob.domain.area.repository.AreaRepository;
import com.bob.global.exception.exceptions.ApplicationException;
import com.bob.global.exception.response.ApplicationError;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class AreaReader {

  private final AreaRepository areaRepository;

  public EmdArea readEmdArea(Integer emdId) {
    return areaRepository.findById(emdId)
        .orElseThrow(() -> new ApplicationException(ApplicationError.NOT_EXISTS_AREA));
  }
}
