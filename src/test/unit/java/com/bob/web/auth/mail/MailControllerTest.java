package com.bob.web.auth.mail;

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
import com.bob.global.infra.mail.MailService;

@DisplayName("이메일 API 테스트")
@ExtendWith(MockitoExtension.class)
class MailControllerTest {

  @InjectMocks
  private MailController mailController;

  @Mock
  private MailService mailService;

  @Test
  @DisplayName("이메일 인증 코드 전송 요청 테스트")
  void 이메일_인증_코드_전송_API를_호출할_수_있다() throws Exception {
    MockMvc mvc = standaloneSetup(mailController).build();

    mvc.perform(post("/auth/email")
        .param("email", "test@email.com")
        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
    ).andExpect(status().isOk());

    verify(mailService, times(1)).sendMailProcess("test@email.com");
  }

  @Test
  @DisplayName("이메일 인증 코드 검증 요청 테스트")
  void 이메일_인증_코드_검증_API를_호출할_수_있다() throws Exception {
    MockMvc mvc = standaloneSetup(mailController).build();

    mvc.perform(post("/auth/email/confirm")
        .param("email", "test@email.com")
        .param("code", "ABC123")
        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
    ).andExpect(status().isOk());

    verify(mailService, times(1)).verifyMailProcess("test@email.com", "ABC123");
  }
}
