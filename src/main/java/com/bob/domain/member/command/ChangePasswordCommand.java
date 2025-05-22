package com.bob.domain.member.command;

public record ChangePasswordCommand(
    Long memberId,
    String oldPassword,
    String newPassword
) {

}
