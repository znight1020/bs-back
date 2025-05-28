package com.bob.web.member.request;

import com.bob.domain.member.service.dto.query.ReadProfileQuery;
import java.util.UUID;

public record ReadProfileRequest(

) {

  public static ReadProfileQuery toQuery(UUID memberId) {
    return new ReadProfileQuery(memberId);
  }
}
