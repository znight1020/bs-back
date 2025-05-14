package com.bob.domain.area.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.locationtech.jts.geom.Geometry;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Entity
@Table(name = "emd_areas")
public class EmdArea {

  @Id
  private Integer id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "sigg_area_id", nullable = false)
  private SiggArea siggArea;

  @Column(length = 10, nullable = false)
  private String admCode;

  @Column(length = 50, nullable = false)
  private String name;

  @Column(columnDefinition = "geometry SRID 4326")
  private Geometry geom;
}
