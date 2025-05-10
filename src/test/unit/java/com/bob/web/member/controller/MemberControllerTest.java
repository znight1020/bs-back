package com.bob.web.member.controller;

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
import com.bob.domain.member.command.CreateMemberCommand;
import com.bob.domain.member.service.MemberService;

@DisplayName("회원 API 테스트")
@ExtendWith(MockitoExtension.class)
class MemberControllerTest {

  @InjectMocks
  private MemberController memberController;

  @Mock
  private MemberService memberService;

  @Test
  @DisplayName("회원가입 API 호출 테스트")
  void 회원가입_API를_호출할_수_있다() throws Exception {
    // given
    MockMvc mvc = standaloneSetup(memberController).build();

    String json = """
        {
            "email": "test@email.com",
            "password": "1234",
            "nickname": "테스터"
        }
        """;

    // when
    mvc.perform(post("/members/signup")
        .contentType(MediaType.APPLICATION_JSON)
        .content(json)
    ).andExpect(status().isCreated());

    // then
    verify(memberService, times(1)).signupProcess(any(CreateMemberCommand.class));
  }
}
