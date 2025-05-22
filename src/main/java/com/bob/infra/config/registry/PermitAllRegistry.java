package com.bob.infra.config.registry;

import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.HttpMethod.POST;

import jakarta.servlet.http.HttpServletRequest;
import java.util.List;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.stereotype.Component;

@Component
public class PermitAllRegistry {

  private final List<RequestMatcher> whitelistMatchers = List.of(
      new AntPathRequestMatcher("/auth/**", POST.name()),
      new AntPathRequestMatcher("/ai/**", GET.name()),
      new AntPathRequestMatcher("/h2-console/**"),
      new AntPathRequestMatcher("/error/**"),
      new AntPathRequestMatcher("/members", POST.name()),
      new AntPathRequestMatcher("/posts", GET.name()),
      new AntPathRequestMatcher("/posts/{postId:\\d+}", GET.name()),
      new AntPathRequestMatcher("/members/{memberId:\\d+}", GET.name())
  );

  public boolean isWhiteList(HttpServletRequest request) {
    return whitelistMatchers.stream()
        .anyMatch(matcher -> matcher.matches(request));
  }
}
