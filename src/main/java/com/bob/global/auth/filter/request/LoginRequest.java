package com.bob.global.auth.filter.request;

public record LoginRequest(
    String email,
    String password
) {
}
