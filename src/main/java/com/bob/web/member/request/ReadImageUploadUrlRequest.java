package com.bob.web.member.request;

import com.bob.domain.member.service.dto.command.ChangeProfileImageUrlCommand;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import java.util.UUID;

public record ReadImageUploadUrlRequest(
    @NotBlank(message = "Content-Type은 필수입니다.")
    @Pattern(
        regexp = "image/(jpeg|png|gif|bmp|webp)",
        message = "이미지 형식의 Content-Type만 허용됩니다. (jpeg, png, gif, bmp, webp)"
    )
    String contentType
) {

  public ChangeProfileImageUrlCommand toQuery(UUID memberId) {
    return new ChangeProfileImageUrlCommand(memberId, contentType);
  }
}
