package com.bob.global.utils;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.locationtech.jts.geom.Point;

@DisplayName("GeometryUtils 테스트")
class GeometryUtilsTest {

  @Test
  @DisplayName("Point 생성 - 위도, 경도 좌표")
  void 위도_경도로_Point를_생성할_수_있다() throws NoSuchFieldException {
    // given
    double latitude = 37.123456;
    double longitude = 127.654321;

    // when
    Point point = GeometryUtils.createPoint(latitude, longitude);

    // then
    assertThat(point).isNotNull();
    assertThat(point.getX()).isEqualTo(longitude);
    assertThat(point.getY()).isEqualTo(latitude);
    assertThat(point.getSRID()).isEqualTo(4326);
    assertThat(GeometryUtils.class.getDeclaredField("geometryFactory")).isNotNull();
  }

  @Test
  @DisplayName("Point 위도, 경도 추출")
  void Point에서_위도_경도를_추출할_수_있다() {
    // given
    double latitude = 35.0;
    double longitude = 128.0;
    Point point = GeometryUtils.createPoint(latitude, longitude);

    // when
    double lat = GeometryUtils.getLatitude(point);
    double lon = GeometryUtils.getLongitude(point);

    // then
    assertThat(lat).isEqualTo(latitude);
    assertThat(lon).isEqualTo(longitude);
  }
}