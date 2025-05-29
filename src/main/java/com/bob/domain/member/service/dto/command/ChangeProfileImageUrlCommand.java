package com.bob.domain.member.service.dto.command;

import java.util.UUID;

public record ChangeProfileImageUrlCommand(
    UUID memberId,
    String contentType
) {

}
