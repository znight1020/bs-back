package toy.bookswap.global.auth.jwt;

import static org.assertj.core.api.Assertions.assertThat;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;
import toy.bookswap.global.auth.jwt.JwtProvider;

@DisplayName("Jwt 유틸 테스트")
class JwtProviderTest {

  private JwtProvider jwtProvider;

  private static final String SECRET_KEY = "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa";

  @BeforeEach
  void setUp() {
    jwtProvider = new JwtProvider();
    ReflectionTestUtils.setField(jwtProvider, "key", "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa");
    ReflectionTestUtils.setField(jwtProvider, "accessExpireTime", 60L);
    ReflectionTestUtils.setField(jwtProvider, "refreshExpireTime", 120L);
  }

  @Test
  @DisplayName("Access Token 생성 - 성공 테스트")
  void AccessToken을_생성할_수_있다() {
    // when
    String token = jwtProvider.generateAccessToken(1L);

    // then
    assertThat(token).isNotBlank();
  }

  @Test
  @DisplayName("Refresh Token 생성 - 성공 테스트")
  void RefreshToken을_생성할_수_있다() {
    // when
    String token = jwtProvider.generateRefreshToken(1L);

    // then
    assertThat(token).isNotBlank();
  }

  @Test
  @DisplayName("MemberId 추출 - 성공 테스트")
  void Token에서_MemberId를_추출할_수_있다() {
    // given
    String token = Jwts.builder()
        .setIssuer("bookswap")
        .setSubject("access-token")
        .claim("memberId", 123L)
        .setIssuedAt(new Date())
        .setExpiration(new Date(System.currentTimeMillis() + 60000))
        .signWith(Keys.hmacShaKeyFor(SECRET_KEY.getBytes(StandardCharsets.UTF_8)))
        .compact();

    // when
    Long memberId = jwtProvider.getMemberId(token);

    // then
    assertThat(memberId).isEqualTo(123L);
  }

  @Test
  @DisplayName("Token 서명 검증 - 성공 테스트")
  void 토큰이_서명되었는지_검증할_수_있다() {
    // given
    String token = Jwts.builder()
        .setIssuer("bookswap")
        .setSubject("access-token")
        .claim("memberId", 123L)
        .setIssuedAt(new Date())
        .setExpiration(new Date(System.currentTimeMillis() + 60000))
        .signWith(Keys.hmacShaKeyFor(SECRET_KEY.getBytes(StandardCharsets.UTF_8)))
        .compact();

    // when
    boolean isVerified = jwtProvider.isVerified(token);

    // then
    assertThat(isVerified).isTrue();
  }

  @Test
  @DisplayName("Token 만료 여부 확인 - 성공 테스트(만료되지 않은 경우)")
  void 만료되지_않은_토큰이면_false를_반환한다() {
    // given
    String token = Jwts.builder()
        .setIssuer("bookswap")
        .setSubject("access-token")
        .claim("memberId", 123L)
        .setIssuedAt(new Date())
        .setExpiration(new Date(System.currentTimeMillis() + 60000))
        .signWith(Keys.hmacShaKeyFor(SECRET_KEY.getBytes(StandardCharsets.UTF_8)))
        .compact();

    // when
    boolean isExpired = jwtProvider.isExpired(token);

    // then
    assertThat(isExpired).isFalse();
  }

  @Test
  @DisplayName("Token 만료 여부 확인 - 성공 테스트(만료된 경우)")
  void 만료된_토큰이면_true를_반환한다() {
    // given
    String token = Jwts.builder()
        .setIssuer("bookswap")
        .setSubject("access-token")
        .claim("memberId", 123L)
        .setIssuedAt(new Date(System.currentTimeMillis() - 120000))
        .setExpiration(new Date(System.currentTimeMillis() - 60000))
        .signWith(Keys.hmacShaKeyFor(SECRET_KEY.getBytes(StandardCharsets.UTF_8)))
        .compact();

    // when
    boolean isExpired = jwtProvider.isExpired(token);

    // then
    assertThat(isExpired).isTrue();
  }
}
