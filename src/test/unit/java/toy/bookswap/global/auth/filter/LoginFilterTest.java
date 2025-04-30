package toy.bookswap.global.auth.filter;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.BDDMockito.any;
import static org.mockito.BDDMockito.contains;
import static org.mockito.BDDMockito.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.BDDMockito.times;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static toy.bookswap.global.exception.response.AuthenticationError.FAILED_AUTHENTICATION;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.DelegatingServletInputStream;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.test.util.ReflectionTestUtils;
import toy.bookswap.global.auth.filter.request.LoginRequest;
import toy.bookswap.global.auth.jwt.JwtProvider;
import toy.bookswap.global.auth.response.MemberDetails;
import toy.bookswap.global.exception.exceptions.ApplicationAuthenticationException;

@DisplayName("로그인 필터 테스트")
@ExtendWith(MockitoExtension.class)
class LoginFilterTest {

  @InjectMocks
  private LoginFilter loginFilter;

  @Mock
  HttpServletResponse response;

  @Mock
  Authentication authentication;

  @Mock
  HttpServletRequest request;

  @Mock
  private AuthenticationManager authManager;

  @Mock
  private AuthenticationEntryPoint authenticationEntryPoint;

  @Mock
  private JwtProvider jwtProvider;

  private final ObjectMapper objectMapper = new ObjectMapper();

  @BeforeEach
  void setUp() {
    ReflectionTestUtils.setField(loginFilter, "objectMapper", objectMapper);
  }

  @Test
  @DisplayName("로그인 요청 - 성공 테스트")
  void 로그인_요청을_시도할_수_있다() throws Exception {
    // given
    LoginRequest loginRequest = new LoginRequest("test@email.com", "1234");
    byte[] loginRequestBytes = objectMapper.writeValueAsBytes(loginRequest);

    given(request.getInputStream()).willReturn(new DelegatingServletInputStream(new ByteArrayInputStream(loginRequestBytes)));
    given(authManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).willReturn(authentication);

    // when
    Authentication result = loginFilter.attemptAuthentication(request, response);

    // then
    assertThat(result).isNotNull();
    then(authManager).should().authenticate(any(UsernamePasswordAuthenticationToken.class));
  }

  @Test
  @DisplayName("로그인 인증 - 성공 테스트")
  void 인증에_성공하면_토큰을_발급하고_Security_Context에_정보를_저장한다() {
    // given
    FilterChain filterChain = mock(FilterChain.class);

    MemberDetails memberDetails = new MemberDetails(1L);
    Authentication authentication = new UsernamePasswordAuthenticationToken(
        memberDetails, null,
        memberDetails.getAuthorities()
    );
    given(jwtProvider.generateAccessToken(1L)).willReturn("access-token");
    given(jwtProvider.generateRefreshToken(1L)).willReturn("refresh-token");

    // when
    loginFilter.successfulAuthentication(request, response, filterChain, authentication);

    // then
    verify(response, times(1)).addHeader(eq("Set-Cookie"), contains("Authorization=access-token"));
    verify(response).setStatus(HttpStatus.OK.value());
    assertThat(authentication.getPrincipal()).isInstanceOf(MemberDetails.class);
  }

  @Test
  @DisplayName("로그인 인증 - 실패 테스트")
  void 인증에_실패하면_예외_핸들러를_호출한다() throws Exception {
    // given
    AuthenticationException failed = mock(AuthenticationException.class);

    // when
    loginFilter.unsuccessfulAuthentication(request, response, failed);

    // then
    then(authenticationEntryPoint).should()
        .commence(eq(request), eq(response),
            argThat(e ->
                ((ApplicationAuthenticationException) e).getError() == FAILED_AUTHENTICATION
            )
        );
  }
}
