package com.bob.global.exception.response;

public record ErrorResponse(
    String code,
    String message
) {

}
