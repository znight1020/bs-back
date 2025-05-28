package com.bob.domain.member.service.dto.command;

import java.util.UUID;

public record ChangeProfileCommand(
    UUID memberId,
    String nickname
) {

}
