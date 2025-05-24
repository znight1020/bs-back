package com.bob.domain.member.service.dto.command;

public record ChangePasswordCommand(
    Long memberId,
    String oldPassword,
    String newPassword
) {

}
