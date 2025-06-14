package com.bob.web.chat.controller;

import static com.bob.support.fixture.domain.MemberFixture.MEMBER_ID;
import static com.bob.support.fixture.request.CreateChatRoomRequestFixture.DEFAULT_CREATE_CHAT_ROOM_REQUEST;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.bob.domain.chat.service.ChatRoomService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@DisplayName("채팅방 API 테스트")
@ExtendWith(MockitoExtension.class)
class ChatRoomControllerTest {

  @InjectMocks
  private ChatRoomController chatRoomController;

  @Mock
  private ChatRoomService chatRoomService;

  private MockMvc mvc;

  @BeforeEach
  void setUp() {
    mvc = MockMvcBuilders.standaloneSetup(chatRoomController).build();
  }

  @Test
  @DisplayName("채팅방 생성 API를 호출할 수 있다")
  void 채팅방_생성_API를_호출할_수_있다() throws Exception {
    // given
    String json = DEFAULT_CREATE_CHAT_ROOM_REQUEST();

    // when & then
    mvc.perform(post("/chatrooms")
            .contentType(MediaType.APPLICATION_JSON)
            .content(json)
            .requestAttr("memberId", MEMBER_ID))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.success").value(true))
        .andExpect(jsonPath("$.result").value("CREATED"));

    then(chatRoomService).should(times(1)).createChatRoomProcess(any());
  }
}