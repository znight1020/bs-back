package com.bob.domain.member.repository;

import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import com.bob.domain.member.entity.Member;

public interface MemberRepository extends JpaRepository<Member, UUID> {

  Optional<Member> findByEmail(String email);

  boolean existsByEmail(String email);
}
