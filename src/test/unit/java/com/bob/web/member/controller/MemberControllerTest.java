package com.bob.web.member.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

import com.bob.domain.member.dto.command.ChangePasswordCommand;
import com.bob.domain.member.dto.command.IssuePasswordCommand;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import com.bob.domain.member.dto.command.CreateMemberCommand;
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
            "nickname": "테스터",
            "emdId": 1
        }
        """;

    // when
    mvc.perform(post("/members")
        .contentType(MediaType.APPLICATION_JSON)
        .content(json)
    ).andExpect(status().isCreated());

    // then
    verify(memberService, times(1)).signupProcess(any(CreateMemberCommand.class));
  }

  @Test
  @DisplayName("비밀번호 변경 API 호출 테스트")
  void 비밀번호_변경_API를_호출할_수_있다() throws Exception {
    // given
    MockMvc mvc = standaloneSetup(memberController).build();

    String json = """
        {
            "oldPassword": "password",
            "newPassword": "new-password"
        }
        """;

    // when
    mvc.perform(patch("/members/me/password")
        .contentType(MediaType.APPLICATION_JSON)
        .content(json)
    ).andExpect(status().isOk());

    // then
    verify(memberService, times(1)).changePasswordProcess(any(ChangePasswordCommand.class));
  }

  @Test
  @DisplayName("임시 비밀번호 발급 API 호출 테스트")
  void 임시_비밀번호_발급_API를_호출할_수_있다() throws Exception {
    // given
    MockMvc mvc = standaloneSetup(memberController).build();

    String json = """
        {
            "email": "test@email.com"
        }
        """;

    // when
    mvc.perform(patch("/members/temp/password")
        .contentType(MediaType.APPLICATION_JSON)
        .content(json)
    ).andExpect(status().isOk());

    // then
    verify(memberService, times(1)).issueTempPasswordProcess(any(IssuePasswordCommand.class));
  }
}
