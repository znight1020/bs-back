package com.bob.domain.member.service.reader;

import static com.bob.global.exception.response.ApplicationError.NOT_EXISTS_MEMBER;
import static com.bob.support.fixture.domain.MemberFixture.defaultIdMember;
import static java.util.UUID.randomUUID;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import com.bob.domain.member.entity.Member;
import com.bob.domain.member.repository.MemberRepository;
import com.bob.global.exception.exceptions.ApplicationException;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@DisplayName("MemberReader 테스트")
@ExtendWith(MockitoExtension.class)
class MemberReaderTest {

  @InjectMocks
  private MemberReader memberReader;

  @Mock
  private MemberRepository memberRepository;

  @Test
  @DisplayName("회원 ID 조회 - 성공 테스트")
  void ID를_이용하여_회원_조회에_성공한다() {
    // given
    Member member = defaultIdMember();
    given(memberRepository.findById(member.getId())).willReturn(Optional.of(member));

    // when
    Member result = memberReader.readMemberById(member.getId());

    // then
    assertThat(result).isEqualTo(member);
  }

  @Test
  @DisplayName("회원 ID 조회 - 실패 테스트(존재하지 않는 ID)")
  void 존재하지_않는_ID의_회원이면_예외를_반환한다() {
    // given
    given(memberRepository.findById(any(UUID.class))).willReturn(Optional.empty());

    // when & then
    assertThatThrownBy(() -> memberReader.readMemberById(randomUUID()))
        .isInstanceOf(ApplicationException.class)
        .hasMessageContaining(NOT_EXISTS_MEMBER.getMessage());
  }

  @Test
  @DisplayName("회원 Email 조회 - 성공 테스트")
  void 이메일을_이용하여_회원_조회에_성공한다() {
    // given
    Member member = defaultIdMember();
    given(memberRepository.findByEmail(defaultIdMember().getEmail())).willReturn(Optional.of(member));

    // when
    Member result = memberReader.readMemberByEmail(defaultIdMember().getEmail());

    // then
    assertThat(result).isEqualTo(member);
  }

  @Test
  @DisplayName("회원 조회 - 실패 테스트(존재하지 않는 ID)")
  void 등록되지_않은_이메일이면_예외를_반환한다() {
    // given
    given(memberRepository.findByEmail(any(String.class))).willReturn(Optional.empty());

    // when & then
    assertThatThrownBy(() -> memberReader.readMemberByEmail("unknown@email.com"))
        .isInstanceOf(ApplicationException.class)
        .hasMessageContaining(NOT_EXISTS_MEMBER.getMessage());
  }
}