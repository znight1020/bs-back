package com.bob.web.member.request;

import com.bob.domain.member.service.dto.query.ReadProfileQuery;

public record ReadProfileRequest(

) {

  public static ReadProfileQuery toQuery(Long memberId) {
    return new ReadProfileQuery(memberId);
  }
}
