package com.bob.web.member.request;

import com.bob.domain.member.service.dto.command.ChangePasswordCommand;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import java.util.UUID;

public record ChangePasswordRequest(
    String oldPassword,

    @NotBlank(message = "새 비밀번호는 필수입니다.")
    @Size(min = 8, max = 20, message = "새 비밀번호는 8자 이상, 20자 이하로 입력해 주세요.")
    @Pattern(
        regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)[A-Za-z\\d]+$",
        message = "새 비밀번호는 영어 대소문자, 숫자를 포함해야 합니다."
    )
    String newPassword
) {

  public ChangePasswordCommand toCommand(UUID memberId) {
    return new ChangePasswordCommand(memberId, oldPassword, newPassword);
  }
}
