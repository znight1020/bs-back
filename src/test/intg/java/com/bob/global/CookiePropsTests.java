package com.bob.global;

import static org.assertj.core.api.Assertions.assertThat;

import com.bob.global.props.CookieProps;
import com.bob.support.TestContainerSupport;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@DisplayName("CookieProps 테스트")
class CookiePropsTests extends TestContainerSupport {

  @Nested
  @ActiveProfiles({"dev", "intg"})
  class DevProfileTest {

    @Autowired CookieProps cookieProps;

    @Test
    @DisplayName("개발 환경 SameSite 값 조회")
    void 개발환경에서는_SameSite가_NONE이다() {
      assertThat(cookieProps.getSameSite()).isEqualTo("NONE");
    }
  }

  @Nested
  @ActiveProfiles({"prod", "intg"})
  class ProdProfileTest {

    @Autowired CookieProps cookieProps;

    @Test
    @DisplayName("운영 환경 SameSite 값 조회")
    void 운영환경에서는_SameSite가_LAX이다() {
      assertThat(cookieProps.getSameSite()).isEqualTo("LAX");
    }
  }
}
