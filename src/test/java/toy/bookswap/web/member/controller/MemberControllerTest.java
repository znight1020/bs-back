package toy.bookswap.web.member.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import toy.bookswap.domain.member.command.CreateMemberCommand;
import toy.bookswap.domain.member.service.MemberService;
import toy.bookswap.web.member.request.SignupRequest;

@ExtendWith(MockitoExtension.class)
class MemberControllerTest {

  @Mock
  private MemberService memberService;

  @InjectMocks
  private MemberController memberController;

  @Test
  @DisplayName("회원가입 API 호출 테스트")
  void callSignupAPITest() throws Exception {
    MockMvc mvc = standaloneSetup(memberController).build();

    String json = """
        {
            "email": "test@email.com",
            "password": "1234",
            "nickname": "테스터"
        }
        """;

    mvc.perform(post("/members")
            .contentType(MediaType.APPLICATION_JSON)
            .content(json)
        ).andExpect(status().isOk());

    verify(memberService, times(1)).signupProcess(any(CreateMemberCommand.class));
  }

  @Test
  @DisplayName("회원가입 서비스 호출 테스트")
  void callSignupProcessTest() {
    // given
    SignupRequest request = new SignupRequest("test@email.com", "1234", "테스터");

    // when
    memberController.singup(request);

    // then
    verify(memberService, times(1)).signupProcess(any(CreateMemberCommand.class));
  }
}
