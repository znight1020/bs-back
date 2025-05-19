package com.bob.domain.member.service.reader;

import static com.bob.global.exception.response.ApplicationError.NOT_EXISTS_MEMBER;
import static com.bob.support.fixture.domain.MemberFixture.defaultIdMember;
import static com.bob.support.fixture.domain.MemberFixture.defaultMember;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;

import com.bob.domain.member.entity.Member;
import com.bob.domain.member.repository.MemberRepository;
import com.bob.global.exception.exceptions.ApplicationException;
import java.util.Optional;
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
  @DisplayName("회원 조회 - 성공 테스트")
  void 회원_조회에_성공한다() {
    // given
    Member member = defaultIdMember();
    given(memberRepository.findById(1L)).willReturn(Optional.of(member));

    // when
    Member result = memberReader.readMember(1L);

    // then
    assertThat(result).isEqualTo(member);
  }

  @Test
  @DisplayName("회원 조회 - 실패 테스트(존재하지 않는 ID)")
  void 존재하지_않는_회원이면_예외를_반환한다() {
    // given
    given(memberRepository.findById(1L)).willReturn(Optional.empty());

    // when & then
    assertThatThrownBy(() -> memberReader.readMember(1L))
        .isInstanceOf(ApplicationException.class)
        .hasMessageContaining(NOT_EXISTS_MEMBER.getMessage());
  }
}