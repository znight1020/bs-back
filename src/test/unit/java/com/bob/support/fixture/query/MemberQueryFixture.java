package com.bob.support.fixture.query;

import com.bob.domain.member.dto.query.ReadProfileQuery;

public class MemberQueryFixture {

  public static ReadProfileQuery defaultReadProfileQuery() {
    return new ReadProfileQuery(1L);
  }
}
