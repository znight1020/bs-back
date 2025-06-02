package com.bob.web.post.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

import com.bob.domain.post.service.PostService;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@DisplayName("게시글 API 테스트")
@ExtendWith(MockitoExtension.class)
class PostControllerTest {

  @InjectMocks
  private PostController postController;

  @Mock
  private PostService postService;

  private MockMvc mvc;

  @BeforeEach
  void setUp() {
    mvc = standaloneSetup(postController).build();
  }

  @Test
  @DisplayName("게시글 생성 API 호출 테스트")
  void 게시글_생성_API를_호출할_수_있다() throws Exception {
    // given
    String json = """
        {
          "categoryId": 1,
          "sellPrice": 10000,
          "bookStatus": "최상",
          "postDescription": "Description",
          "book": {
            "isbn": "1111111111111",
            "title": "Title",
            "author": "Author",
            "description": "Book Description",
            "priceStandard": 20000,
            "cover": "https://image/1.jpg",
            "pubDate": "2025-05-25"
          }
        }
        """;

    // when & then
    mvc.perform(post("/posts")
            .contentType(MediaType.APPLICATION_JSON)
            .content(json)
            .requestAttr("memberId", UUID.randomUUID()))
        .andExpect(status().isCreated());

    verify(postService, times(1)).createPostProcess(any());
  }
}