package toy.bookswap.web.member.request;

import toy.bookswap.domain.member.command.CreateMemberCommand;

public record SignupRequest(
    String nickname,
    String email,
    String password
) {

  public CreateMemberCommand toCommand() {
    return new CreateMemberCommand(email, password, nickname);
  }
}
