package toy.bookswap.web.auth.mail;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import toy.bookswap.global.infra.mail.MailService;
import toy.bookswap.web.common.CommonResponse;
import toy.bookswap.web.common.symbol.ResponseSymbol;

@RequiredArgsConstructor
@RequestMapping("/auth/email")
@RestController
public class MailController {

  private final MailService mailService;

  @PostMapping
  public CommonResponse<ResponseSymbol> sendCode(@RequestParam("email") String email) {
    mailService.sendMailProcess(email);
    return new CommonResponse<>(true, ResponseSymbol.SENT);
  }

  @PostMapping("/confirm")
  public CommonResponse<ResponseSymbol> verifyCode(
      @RequestParam("email") String email,
      @RequestParam("code") String code
  ) {
    mailService.verifyMailProcess(email, code);
    return new CommonResponse<>(true, ResponseSymbol.VERIFIED);
  }
}