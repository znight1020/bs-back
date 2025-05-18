package com.bob.support.fixture.domain;

import com.bob.domain.area.entity.EmdArea;

public class EmdAreaFixture {
  public static EmdArea defaultEmdArea() {
    return EmdArea.builder()
        .id(1)
        .admCode("11000")
        .name("종로구")
        .siggArea(null)
        .geom(null)
        .build();
  }
}
