package com.bob.domain.member.service.reader;

import com.bob.domain.member.entity.Member;
import com.bob.domain.member.repository.MemberRepository;
import com.bob.global.exception.exceptions.ApplicationException;
import com.bob.global.exception.response.ApplicationError;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class MemberReader {

  private final MemberRepository memberRepository;

  public Member readMemberById(UUID id) {
    return memberRepository.findById(id)
        .orElseThrow(() -> new ApplicationException(ApplicationError.NOT_EXISTS_MEMBER));
  }

  public Member readMemberByEmail(String email) {
    return memberRepository.findByEmail(email)
        .orElseThrow(() -> new ApplicationException(ApplicationError.NOT_EXISTS_MEMBER));
  }
}
