package toy.bookswap.global.auth.jwt;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import java.util.Date;
import javax.crypto.SecretKey;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class JwtProvider {

  private static final String ISSUER = "bookswap";

  @Value("${jwt.secret-key}")
  private String key;

  @Value("${jwt.access-token-expire-time}")
  private Long accessExpireTime;

  @Value("${jwt.refresh-token-expire-time}")
  private Long refreshExpireTime;

  public String generateAccessToken(Long memberId) {
    return Jwts.builder()
        .setIssuer(ISSUER)
        .setSubject("access-token")
        .claim("memberId", memberId)
        .setIssuedAt(new Date(System.currentTimeMillis()))
        .setExpiration(new Date(System.currentTimeMillis() + accessExpireTime * 1000))
        .signWith(getSecretKey(key))
        .compact();
  }

  public String generateRefreshToken(Long memberId) {
    return Jwts.builder()
        .setIssuer(ISSUER)
        .setSubject("refresh-token")
        .claim("memberId", memberId)
        .setIssuedAt(new Date(System.currentTimeMillis()))
        .setExpiration(new Date(System.currentTimeMillis() + refreshExpireTime * 1000))
        .signWith(getSecretKey(key))
        .compact();
  }

  public Long getMemberId(String token) {
    return Jwts.parserBuilder()
        .setSigningKey(getSecretKey(key))
        .build()
        .parseClaimsJws(token)
        .getBody()
        .get("memberId", Long.class);
  }

  public boolean isVerified(String token) {
    try {
      Jwts.parserBuilder()
          .build()
          .isSigned(token);
      return true;
    } catch (Exception e) {
      return false;
    }
  }

  public boolean isExpired(String token) {
    try {
      return Jwts.parserBuilder()
          .setSigningKey(getSecretKey(key))
          .build()
          .parseClaimsJws(token)
          .getBody()
          .getExpiration()
          .before(new Date());
    } catch (Exception e) {
      return true;
    }
  }

  private SecretKey getSecretKey(String secret) {
    return Keys.hmacShaKeyFor(secret.getBytes());
  }
}
