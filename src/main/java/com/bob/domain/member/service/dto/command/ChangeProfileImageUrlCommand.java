package com.bob.domain.member.service.dto.command;

public record ChangeProfileImageUrlCommand(
    Long memberId,
    String contentType
) {

}
