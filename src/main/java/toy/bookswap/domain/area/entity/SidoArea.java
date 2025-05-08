package toy.bookswap.domain.area.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Entity
@Table(name = "sido_areas")
public class SidoArea {

  @Id
  private Integer id;

  @Column(length = 2, nullable = false)
  private String admCode;

  @Column(length = 50, nullable = false)
  private String name;
}
