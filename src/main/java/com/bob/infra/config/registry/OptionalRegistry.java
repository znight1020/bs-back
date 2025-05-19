package com.bob.infra.config.registry;

import jakarta.servlet.http.HttpServletRequest;
import java.util.List;
import org.springframework.http.HttpMethod;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.stereotype.Component;

@Component
public class OptionalRegistry {
  private final List<RequestMatcher> optionalAuthMatchers = List.of(
      new AntPathRequestMatcher("/areas/**", HttpMethod.PATCH.name())
  );

  public boolean isOptionalAuth(HttpServletRequest request) {
    return optionalAuthMatchers.stream().anyMatch(m -> m.matches(request));
  }
}
