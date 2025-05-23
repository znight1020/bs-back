package com.bob.web.auth.mail.controller;

import com.bob.domain.member.service.port.MailService;
import com.bob.web.auth.mail.request.MailSendRequest;
import com.bob.web.auth.mail.request.MailVerifyRequest;
import com.bob.web.common.CommonResponse;
import com.bob.web.common.symbol.ResponseSymbol;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("/auth/email")
@RestController
public class MailController {

  private final MailService mailService;

  @PostMapping
  public CommonResponse<ResponseSymbol> handleSendCode(@RequestBody MailSendRequest request) {
    mailService.sendCodeProcess(request.email());
    return new CommonResponse<>(true, ResponseSymbol.SENT);
  }

  @PostMapping("/confirm")
  public CommonResponse<ResponseSymbol> handleVerifyCode(@RequestBody MailVerifyRequest request) {
    mailService.verifyCodeProcess(request.email(), request.code());
    return new CommonResponse<>(true, ResponseSymbol.VERIFIED);
  }
}
