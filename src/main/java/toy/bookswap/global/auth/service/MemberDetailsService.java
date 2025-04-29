package toy.bookswap.global.auth.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import toy.bookswap.domain.member.entity.Member;
import toy.bookswap.domain.member.repository.MemberRepository;
import toy.bookswap.global.auth.response.MemberDetails;

@Service
@RequiredArgsConstructor
public class MemberDetailsService implements UserDetailsService {

  private final MemberRepository memberRepository;

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    Member member = memberRepository.findByEmail(username)
        .orElseThrow(() -> new UsernameNotFoundException(username));

    return new MemberDetails(member.getId(), member.getPassword(), member.getEmail());
  }

}
