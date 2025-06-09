package com.bob.web.member.controller;

import static com.bob.support.fixture.domain.MemberFixture.MEMBER_ID;
import static com.bob.support.fixture.response.MemberPostsResponseFixture.DEFAULT_MEMBER_POSTS_RESPONSE;
import static com.bob.support.fixture.response.MemberProfileImageUrlResponseFixture.DEFAULT_MEMBER_PROFILE_IMAGE_URL_RESPONSE;
import static com.bob.support.fixture.response.MemberProfileResponseFixture.DEFAULT_MEMBER_PROFILE_RESPONSE;
import static com.bob.support.fixture.response.MemberProfileWithPostsResponseFixture.DEFAULT_MEMBER_PROFILE_WITH_POSTS_RESPONSE;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

import com.bob.domain.member.service.dto.command.ChangePasswordCommand;
import com.bob.domain.member.service.dto.command.CreateMemberCommand;
import com.bob.domain.member.service.dto.command.IssuePasswordCommand;
import com.bob.domain.member.service.MemberService;
import com.bob.domain.member.service.dto.response.internal.MemberPostsResponse;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@DisplayName("회원 API 테스트")
@ExtendWith(MockitoExtension.class)
class MemberControllerTest {

  @InjectMocks
  private MemberController memberController;

  @Mock
  private MemberService memberService;

  private MockMvc mvc;

  @BeforeEach
  void setUp() {
    PageableHandlerMethodArgumentResolver pageableResolver = new PageableHandlerMethodArgumentResolver();
    mvc = standaloneSetup(memberController)
        .setCustomArgumentResolvers(pageableResolver)
        .build();
  }

  @Test
  @DisplayName("회원가입 API 호출 테스트")
  void 회원가입_API를_호출할_수_있다() throws Exception {
    // given
    String json = """
        {
            "email": "test@email.com",
            "password": "1Q2w3e4r",
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
  @DisplayName("내 프로필 조회 API 호출 테스트")
  void 내_프로필_조회_API를_호출할_수_있다() throws Exception {
    // given
    UUID memberId = MEMBER_ID;
    given(memberService.readProfileProcess(any())).willReturn(DEFAULT_MEMBER_PROFILE_RESPONSE);

    // when & then
    mvc.perform(get("/members/me")
            .requestAttr("memberId", memberId))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.memberId").value(MEMBER_ID.toString()))
        .andExpect(jsonPath("$.nickname").value("tester"))
        .andExpect(jsonPath("$.profileImageUrl").value("http://image.url"))
        .andExpect(jsonPath("$.area.emdId").value(213))
        .andExpect(jsonPath("$.area.isAuthentication").value(true));

    verify(memberService, times(1)).readProfileProcess(any());
  }

  @Test
  @DisplayName("내가 작성한 게시글 목록 조회 API 호출 테스트")
  void 내_게시글_목록을_조회할_수_있다() throws Exception {
    // given
    given(memberService.readMemberPostsProcess(any())).willReturn(DEFAULT_MEMBER_POSTS_RESPONSE);

    // when & then
    mvc.perform(get("/members/me/posts")
            .param("page", "0")
            .param("size", "12")
            .requestAttr("memberId", MEMBER_ID))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.totalCount").value(2))
        .andExpect(jsonPath("$.posts[0].postId").value(1))
        .andExpect(jsonPath("$.posts[0].postTitle").value("객체지향의 사실과 오해"))
        .andExpect(jsonPath("$.posts[1].postId").value(2))
        .andExpect(jsonPath("$.posts[1].postTitle").value("오브젝트"));

    verify(memberService, times(1)).readMemberPostsProcess(any());
  }


  @Test
  @DisplayName("좋아요한 게시글 목록 조회 API 호출 테스트")
  void 좋아요한_게시글_목록을_조회한다() throws Exception {
    // given
    given(memberService.readMemberFavoritePosts(any())).willReturn(DEFAULT_MEMBER_POSTS_RESPONSE);

    // when & then
    mvc.perform(get("/members/me/favorites")
            .param("page", "0")
            .param("size", "10"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.totalCount").value(2))
        .andExpect(jsonPath("$.posts[0].postId").value(1))
        .andExpect(jsonPath("$.posts[0].postTitle").value("객체지향의 사실과 오해"))
        .andExpect(jsonPath("$.posts[1].postId").value(2))
        .andExpect(jsonPath("$.posts[1].postTitle").value("오브젝트"));

    verify(memberService, times(1)).readMemberFavoritePosts(any());
  }

  @Test
  @DisplayName("회원 프로필 조회 API 호출 테스트")
  void 특정_회원_프로필과_게시글_목록을_조회할_수_있다() throws Exception {
    // given
    UUID memberId = MEMBER_ID;
    given(memberService.readProfileByIdWithPostsProcess(any())).willReturn(DEFAULT_MEMBER_PROFILE_WITH_POSTS_RESPONSE);

    // when & then
    mvc.perform(get("/members/{memberId}", memberId)
            .param("page", "0")
            .param("size", "12"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.profile.memberId").value(MEMBER_ID.toString()))
        .andExpect(jsonPath("$.profile.nickname").value("tester"))
        .andExpect(jsonPath("$.memberPosts.totalCount").value(2))
        .andExpect(jsonPath("$.memberPosts.posts[0].postId").value(1))
        .andExpect(jsonPath("$.memberPosts.posts[0].postTitle").value("객체지향의 사실과 오해"))
        .andExpect(jsonPath("$.memberPosts.posts[1].postId").value(2))
        .andExpect(jsonPath("$.memberPosts.posts[1].postTitle").value("오브젝트"));

    verify(memberService, times(1)).readProfileByIdWithPostsProcess(any());
  }

  @Test
  @DisplayName("프로필 변경 API 호출 테스트")
  void 프로필_변경_API를_호출할_수_있다() throws Exception {
    // given
    String json = """
      {
          "nickname": "변경된닉네임"
      }
      """;

    // when
    mvc.perform(patch("/members/me")
            .contentType(MediaType.APPLICATION_JSON)
            .content(json))
        .andExpect(status().isOk());

    // then
    verify(memberService, times(1)).changeProfileProcess(any());
  }

  @Test
  @DisplayName("비밀번호 변경 API 호출 테스트")
  void 비밀번호_변경_API를_호출할_수_있다() throws Exception {
    // given
    String json = """
        {
            "oldPassword": "password",
            "newPassword": "1Q2w3e4r"
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

  @Test
  @DisplayName("프로필 이미지 변경 API 호출 테스트")
  void 프로필_이미지_Presigned_URL_발급_API를_호출할_수_있다() throws Exception {
    // given
    String json = """
    {
      "contentType": "image/png"
    }
    """;
    given(memberService.changeProfileImageUrlProcess(any())).willReturn(DEFAULT_MEMBER_PROFILE_IMAGE_URL_RESPONSE);

    // when & then
    mvc.perform(patch("/members/me/image")
            .contentType(MediaType.APPLICATION_JSON)
            .content(json)
            .requestAttr("memberId", MEMBER_ID))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.fileName").value("profile/test.png"))
        .andExpect(jsonPath("$.imageUploadUrl").value("https://s3-url.com/presigned"));

    verify(memberService, times(1)).changeProfileImageUrlProcess(any());
  }

}
