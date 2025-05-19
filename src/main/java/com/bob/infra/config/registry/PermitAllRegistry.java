package com.bob.infra.config.registry;

import jakarta.servlet.http.HttpServletRequest;
import java.util.List;
import org.springframework.http.HttpMethod;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.stereotype.Component;

@Component
public class PermitAllRegistry {

  private final List<RequestMatcher> whitelistMatchers = List.of(
      new AntPathRequestMatcher("/auth/**"),
      new AntPathRequestMatcher("/ai/**", HttpMethod.GET.name()),
      new AntPathRequestMatcher("/h2-console/**"),
      new AntPathRequestMatcher("/error/**"),
      new AntPathRequestMatcher("/members", HttpMethod.POST.name()),
      new AntPathRequestMatcher("/posts", HttpMethod.GET.name()),
      new AntPathRequestMatcher("/posts/{postId:\\d+}", HttpMethod.GET.name()),
      new AntPathRequestMatcher("/members/{memberId:\\d+}", HttpMethod.GET.name())
  );

  public boolean isWhiteList(HttpServletRequest request) {
    return whitelistMatchers.stream()
        .anyMatch(matcher -> matcher.matches(request));
  }
}
