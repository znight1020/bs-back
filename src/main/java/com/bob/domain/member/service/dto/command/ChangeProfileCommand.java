package com.bob.domain.member.service.dto.command;

public record ChangeProfileCommand(
    Long memberId,
    String nickname
) {

}
