package com.bob.support.fixture.query;

import com.bob.domain.member.dto.query.ReadProfileQuery;
import com.bob.domain.member.dto.query.ReadProfileWithPostsQuery;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

public class MemberQueryFixture {

  public static ReadProfileQuery defaultReadProfileQuery() {
    return new ReadProfileQuery(1L);
  }

  public static ReadProfileWithPostsQuery defaultReadProfileWithPostsQuery(Long memberId) {
    Pageable pageable = PageRequest.of(0, 10);
    return new ReadProfileWithPostsQuery(memberId, pageable);
  }
}
