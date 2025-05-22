package com.bob.infra.config;

import static jakarta.servlet.http.HttpServletResponse.*;

import com.bob.infra.auth.filter.LoginFilter;
import com.bob.infra.auth.jwt.JwtProvider;
import com.bob.infra.auth.jwt.filter.JwtAuthorizationFilter;
import com.bob.infra.auth.jwt.handler.JwtAuthenticationEntryPoint;
import com.bob.infra.auth.service.MemberDetailsService;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@RequiredArgsConstructor
@EnableWebSecurity
@Configuration
public class SecurityConfig {

  private static final String[] AUTH_WHITELIST = {
      "/auth/**", "/ai/**", "/areas/**",
      "/h2-console/**",
      "/error/**",
  };
  private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
  private final JwtAuthorizationFilter jwtAuthorizationFilter;
  private final MemberDetailsService memberDetailsService;
  private final JwtProvider jwtProvider;
  private final ObjectMapper objectMapper;

  @Bean
  public SecurityFilterChain configure(HttpSecurity http, AuthenticationManager authenticationManager) throws Exception {
    return http
        .cors(Customizer.withDefaults())
        .csrf(AbstractHttpConfigurer::disable)
        .anonymous(AbstractHttpConfigurer::disable)
        .httpBasic(AbstractHttpConfigurer::disable)
        .formLogin(AbstractHttpConfigurer::disable)
        .logout(filter -> filter
            .logoutUrl("/auth/logout")
            .logoutSuccessHandler((req, res, auth) -> res.setStatus(SC_OK))
            .addLogoutHandler((req, res, auth) -> req.getSession().invalidate())
            .deleteCookies("JSESSIONID", "AUTHORIZATION", "REFRESH")
        )
        .sessionManagement(m -> m.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
        .headers(header -> header.frameOptions(HeadersConfigurer.FrameOptionsConfig::sameOrigin))
        .authorizeHttpRequests(request -> request
            .requestMatchers(AUTH_WHITELIST).permitAll()
            .requestMatchers(HttpMethod.POST, "/members").permitAll()
            .requestMatchers(HttpMethod.GET, "/posts").permitAll()
            .requestMatchers(HttpMethod.GET, "/posts/{postId:[\\d]+}").permitAll()
            .requestMatchers(HttpMethod.GET, "/members/{memberId:\\d+}").permitAll()
            .anyRequest().authenticated()
        )
        .addFilterBefore(jwtAuthorizationFilter, UsernamePasswordAuthenticationFilter.class)
        .addFilterAt(loginFilter(authenticationManager), UsernamePasswordAuthenticationFilter.class)
        .exceptionHandling(handler -> handler.authenticationEntryPoint(jwtAuthenticationEntryPoint))
        .build();
  }

  @Bean
  public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
    AuthenticationManagerBuilder builder = http.getSharedObject(AuthenticationManagerBuilder.class);
    builder.userDetailsService(memberDetailsService)
        .passwordEncoder(passwordEncoder());
    return builder.build();
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return PasswordEncoderFactories.createDelegatingPasswordEncoder();
  }

  @Bean
  public CorsConfigurationSource corsConfigurationSource() {
    CorsConfiguration config = new CorsConfiguration();
    config.setAllowedOrigins(List.of(
        "http://localhost:3000",
        "https://bookbridge.kr"
    ));
    config.setAllowCredentials(true);
    config.addAllowedMethod("*");
    config.addAllowedHeader("*");

    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    source.registerCorsConfiguration("/**", config);
    return source;
  }

  private LoginFilter loginFilter(AuthenticationManager authenticationManager) {
    LoginFilter loginFilter = new LoginFilter(
        authenticationManager,
        jwtAuthenticationEntryPoint,
        jwtProvider,
        objectMapper
    );
    loginFilter.setFilterProcessesUrl("/auth/login");
    loginFilter.setPostOnly(true);
    return loginFilter;
  }
}
