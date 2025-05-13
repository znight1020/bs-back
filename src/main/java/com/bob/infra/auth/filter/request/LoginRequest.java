package com.bob.infra.auth.filter.request;

public record LoginRequest(
    String email,
    String password
) {
}
