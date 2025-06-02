package com.bob.web.member.request;

import com.bob.domain.member.service.dto.query.ReadProfileWithPostsQuery;
import jakarta.validation.constraints.NotNull;
import java.util.UUID;
import org.springframework.data.domain.Pageable;

public record ReadProfileByIdRequest(
    @NotNull(message = "회원 ID는 필수입니다.")
    UUID memberId
) {

  public static ReadProfileWithPostsQuery toQuery(UUID memberId, Pageable pageable) {
    return new ReadProfileWithPostsQuery(memberId, pageable);
  }
}
