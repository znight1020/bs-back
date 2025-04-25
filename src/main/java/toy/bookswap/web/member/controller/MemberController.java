package toy.bookswap.web.member.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import toy.bookswap.domain.member.service.MemberService;
import toy.bookswap.web.common.CommonResponse;
import toy.bookswap.web.common.symbol.ResponseSymbol;
import toy.bookswap.web.member.request.SignupRequest;

@RequiredArgsConstructor
@RequestMapping("/members")
@RestController
public class MemberController {

  private final MemberService memberService;

  @PostMapping
  public CommonResponse<ResponseSymbol> singup(@RequestBody SignupRequest request) {
    memberService.signupProcess(request.toCommand());
    return new CommonResponse<>(true, ResponseSymbol.CREATED);
  }
}
