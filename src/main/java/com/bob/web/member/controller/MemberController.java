package com.bob.web.member.controller;

import static com.bob.web.common.symbol.ResponseSymbol.CREATED;
import static com.bob.web.common.symbol.ResponseSymbol.SENT;
import static com.bob.web.common.symbol.ResponseSymbol.UPDATED;
import static com.bob.web.member.request.ReadProfileByIdRequest.toQuery;
import static com.bob.web.member.request.ReadProfileRequest.toQuery;

import com.bob.domain.member.service.MemberService;
import com.bob.domain.member.service.dto.response.MemberProfileResponse;
import com.bob.domain.member.service.dto.response.MemberProfileWithPostsResponse;
import com.bob.web.common.AuthenticationId;
import com.bob.web.common.CommonResponse;
import com.bob.web.common.symbol.ResponseSymbol;
import com.bob.web.member.request.ChangePasswordRequest;
import com.bob.web.member.request.ChangeProfileRequest;
import com.bob.web.member.request.IssuePasswordRequest;
import com.bob.web.member.request.SignupRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
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
    return new CommonResponse<>(true, CREATED);
  }

  @GetMapping("/me")
  public ResponseEntity<MemberProfileResponse> handleReadProfile(@AuthenticationId Long memberId) {
    return ResponseEntity.ok(memberService.readProfileProcess(toQuery(memberId)));
  }

  @GetMapping("/{memberId}")
  public ResponseEntity<MemberProfileWithPostsResponse> handleReadProfileById(@PathVariable Long memberId, Pageable pageable) {
    return ResponseEntity.ok(memberService.readProfileByIdWithPostsProcess(toQuery(memberId, pageable)));
  }

  @PatchMapping("/me")
  public CommonResponse<ResponseSymbol> handleChangeProfile(
      @RequestBody ChangeProfileRequest request,
      @AuthenticationId Long memberId
  ) {
    memberService.changeProfileProcess(request.toCommand(memberId));
    return new CommonResponse<>(true, UPDATED);
  }

  @PatchMapping("/me/password")
  public CommonResponse<ResponseSymbol> handleChangePassword(
      @RequestBody ChangePasswordRequest request,
      @AuthenticationId Long memberId
  ) {
    memberService.changePasswordProcess(request.toCommand(memberId));
    return new CommonResponse<>(true, UPDATED);
  }

  @PatchMapping("/temp/password")
  public CommonResponse<ResponseSymbol> handleSendTempPassword(@RequestBody IssuePasswordRequest request) {
    memberService.issueTempPasswordProcess(request.toCommand());
    return new CommonResponse<>(true, SENT);
  }
}
