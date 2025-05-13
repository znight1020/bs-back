package com.bob.domain.member.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.willDoNothing;
import static com.bob.support.mysql.MySQLContainerProvider.MYSQL_CONTAINER;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import com.bob.domain.member.command.CreateMemberCommand;
import com.bob.domain.member.entity.Member;
import com.bob.domain.member.repository.MemberRepository;
import com.bob.global.exception.exceptions.ApplicationException;
import com.bob.domain.member.service.port.MailService;
import com.bob.support.redis.RedisContainerConfig;

@DisplayName("사용자 회원가입 통합 테스트")
@Import(RedisContainerConfig.class)
@ActiveProfiles("intg")
@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class MemberSignupServiceIntgTest {

  @Autowired private MemberService memberService;
  @Autowired private MemberRepository memberRepository;
  @Autowired private PasswordEncoder passwordEncoder;
  @Autowired private RedisTemplate<String, String> redisTemplate;
  @Mock private MailService mailService;

  @DynamicPropertySource
  static void registerDynamicProps(DynamicPropertyRegistry registry) {
    // MySQL
    registry.add("spring.datasource.url", MYSQL_CONTAINER::getJdbcUrl);
    registry.add("spring.datasource.username", MYSQL_CONTAINER::getUsername);
    registry.add("spring.datasource.password", MYSQL_CONTAINER::getPassword);
    registry.add("spring.datasource.driver-class-name", MYSQL_CONTAINER::getDriverClassName);

    // REDIS
    registry.add("spring.data.redis.host", () -> "localhost");
    registry.add("spring.data.redis.port", () -> 6380);
  }

  @BeforeEach
  void init() {
    memberRepository.deleteAll();
  }

  @Test
  @DisplayName("회원가입 - 성공 테스트")
  void 이메일_인증이_완료된_사용자는_회원가입을_할_수_있다() {
    // given
    String email = "test@test.com";
    String code = "ABC123";
    willDoNothing().given(mailService).sendMailProcess(email);
    willDoNothing().given(mailService).verifyMailProcess(email, code);

    redisTemplate.opsForValue().set("email-verified:" + email, "true");
    CreateMemberCommand command = new CreateMemberCommand(email, "password", "tester");

    // when
    memberService.signupProcess(command);

    // then
    Member saved = memberRepository.findByEmail(email).orElseThrow();
    assertThat(saved.getEmail()).isEqualTo(email);
    assertThat(passwordEncoder.matches("password", saved.getPassword())).isTrue();
    assertThat(saved.getNickname()).isEqualTo("tester");
  }

  @Test
  @DisplayName("회원가입 - 실패 테스트(이메일 인증 X)")
  void 이메일_인증을_하지않은_사용자는_회원가입을_할_수_없다() {
    // given
    String email = "unverified@bookswap.com";
    CreateMemberCommand command = new CreateMemberCommand(email, "password", "tester");

    // when & then
    assertThatThrownBy(() -> memberService.signupProcess(command))
        .isInstanceOf(ApplicationException.class)
        .hasMessage("이메일 인증이 완료되지 않았습니다.");
  }

  @Test
  @DisplayName("회원가입 - 실패 테스트(중복된 이메일)")
  void 동일한_이메일_계정이_존재하면_사용자는_회원가입을_할_수_없다() {
    // given
    String email = "test@test.com";
    redisTemplate.opsForValue().set("email-verified:" + email, "true");

    Member existing = Member.builder()
        .email(email)
        .password("password2")
        .nickname("tester2")
        .build();
    memberRepository.save(existing);

    CreateMemberCommand command = new CreateMemberCommand(email, "password2", "tester2");

    // when & then
    assertThatThrownBy(() -> memberService.signupProcess(command))
        .isInstanceOf(ApplicationException.class)
        .hasMessage("해당 이메일로 가입된 계정이 존재합니다.");
  }
}
