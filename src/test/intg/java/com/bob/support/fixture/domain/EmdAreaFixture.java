package com.bob.support.fixture.domain;

import com.bob.domain.area.entity.EmdArea;

public class EmdAreaFixture {
  public static EmdArea defaultEmdArea() {
    return EmdArea.builder()
        .id(213)
        .admCode("640")
        .name("역삼동")
        .siggArea(null)
        .geom(null)
        .build();
  }
}
