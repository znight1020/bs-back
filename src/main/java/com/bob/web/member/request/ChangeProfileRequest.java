package com.bob.web.member.request;

import com.bob.domain.member.service.dto.command.ChangeProfileCommand;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.util.UUID;

public record ChangeProfileRequest(
    @NotBlank(message = "닉네임은 필수입니다.")
    @Size(max = 12, message = "닉네임은 12자 이하로 입력해 주세요.")
    String nickname
) {

  public ChangeProfileCommand toCommand(UUID memberId) {
    return new ChangeProfileCommand(memberId, nickname);
  }
}
