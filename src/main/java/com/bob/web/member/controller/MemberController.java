package com.bob.web.member.controller;

import com.bob.domain.member.dto.response.MemberProfileResponse;
import com.bob.domain.member.service.MemberService;
import com.bob.web.common.AuthenticationId;
import com.bob.web.common.CommonResponse;
import com.bob.web.common.symbol.ResponseSymbol;
import com.bob.web.member.request.ChangePasswordRequest;
import com.bob.web.member.request.IssuePasswordRequest;
import com.bob.web.member.request.ReadProfileRequest;
import com.bob.web.member.request.SignupRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("/members")
@RestController
public class MemberController {

  private final MemberService memberService;

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public CommonResponse<ResponseSymbol> handleSignup(@RequestBody SignupRequest request) {
    memberService.signupProcess(request.toCommand());
    return new CommonResponse<>(true, ResponseSymbol.CREATED);
  }

  @GetMapping("/me")
  public ResponseEntity<MemberProfileResponse> handleReadProfile(@AuthenticationId Long memberId) {
    MemberProfileResponse response = memberService.readProfileProcess(ReadProfileRequest.toQuery(memberId));
    return ResponseEntity.ok(response);
  }

  @PatchMapping("/me/password")
  public CommonResponse<ResponseSymbol> handleChangePassword(
      @RequestBody ChangePasswordRequest request,
      @AuthenticationId Long memberId
  ) {
    memberService.changePasswordProcess(request.toCommand(memberId));
    return new CommonResponse<>(true, ResponseSymbol.UPDATED);
  }

  @PatchMapping("/temp/password")
  public CommonResponse<ResponseSymbol> handleSendTempPassword(@RequestBody IssuePasswordRequest request) {
    memberService.issueTempPasswordProcess(request.toCommand());
    return new CommonResponse<>(true, ResponseSymbol.SENT);
  }
}
