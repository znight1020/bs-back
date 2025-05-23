package com.bob.domain.member.dto.command;

public record ChangePasswordCommand(
    Long memberId,
    String oldPassword,
    String newPassword
) {

}
