package toy.bookswap.domain.member.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import toy.bookswap.domain.member.entity.Member;

public interface MemberRepository extends JpaRepository<Member, Long> {

  Optional<Member> findByEmail(String email);
}
