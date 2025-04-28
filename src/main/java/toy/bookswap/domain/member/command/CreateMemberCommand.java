package toy.bookswap.domain.member.command;

public record CreateMemberCommand(
    String email,
    String password,
    String nickname
) {

}
