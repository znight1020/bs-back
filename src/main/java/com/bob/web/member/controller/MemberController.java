package com.bob.web.member.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import com.bob.domain.member.service.MemberService;
import com.bob.web.common.CommonResponse;
import com.bob.web.common.symbol.ResponseSymbol;
import com.bob.web.member.request.SignupRequest;

@RequiredArgsConstructor
@RequestMapping("/members")
@RestController
public class MemberController {

  private final MemberService memberService;

  @PostMapping("/signup")
  @ResponseStatus(HttpStatus.CREATED)
  public CommonResponse<ResponseSymbol> singup(@RequestBody SignupRequest request) {
    memberService.signupProcess(request.toCommand());
    return new CommonResponse<>(true, ResponseSymbol.CREATED);
  }
}
