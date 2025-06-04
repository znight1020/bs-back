package com.bob.support.fixture.domain;

import com.bob.domain.area.entity.EmdArea;
import com.bob.domain.area.entity.SiggArea;

public class EmdAreaFixture {
  public static EmdArea defaultEmdArea() {
    return EmdArea.builder()
        .id(213)
        .admCode("640")
        .name("역삼동")
        .siggArea(defaultSiggArea())
        .geom(null)
        .build();
  }

  public static EmdArea otherEmdArea() {
    return EmdArea.builder()
        .id(1)
        .admCode("570")
        .name("신곡동")
        .siggArea(defaultSiggArea())
        .geom(null)
        .build();
  }

  public static SiggArea defaultSiggArea() {
    return SiggArea.builder()
        .id(100)
        .admCode("25")
        .name("노원구")
        .sidoArea(null)
        .build();
  }
}
