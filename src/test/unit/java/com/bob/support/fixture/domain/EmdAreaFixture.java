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

  public static EmdArea otherEmdArea() {
    return EmdArea.builder()
        .id(1)
        .admCode("570")
        .name("신곡동")
        .siggArea(null)
        .geom(null)
        .build();
  }
}
