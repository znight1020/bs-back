package com.bob.domain.member.service.dto.command;

import java.util.UUID;

public record ChangePasswordCommand(
    UUID memberId,
    String oldPassword,
    String newPassword
) {

}
