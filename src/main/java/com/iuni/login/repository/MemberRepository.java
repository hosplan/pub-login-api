package com.iuni.login.repository;

import com.iuni.login.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findByEmail(String email);
    Optional<Member> findByEmailAndPassword(String email, String password);

    Optional<Member> findByEmailAndType(String email, String type);

    Optional<Member> findByCredential(String accessToken);
}

