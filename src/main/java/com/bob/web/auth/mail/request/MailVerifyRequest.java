package com.bob.web.auth.mail.request;

public record MailVerifyRequest(
    String email,
    String code
) {

}
