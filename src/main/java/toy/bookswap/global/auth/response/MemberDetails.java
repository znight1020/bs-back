package toy.bookswap.global.auth.response;

import java.util.Collection;
import java.util.List;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

public record MemberDetails(
    Long id,
    String password,
    String email
) implements UserDetails {

  public MemberDetails(Long id) {
    this(id, null, null);
  }

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return List.of();
  }

  @Override
  public String getPassword() {
    return password;
  }

  @Override
  public String getUsername() {
    return email;
  }

  public Long getId() {
    return id;
  }
}


