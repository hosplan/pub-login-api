package com.iuni.login.repository;

import com.iuni.login.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findByEmail(String email);
    Optional<Member> findByEmailAndPassword(String email, String password);

    Optional<Member> findByEmailAndType(String email, String type);

    Optional<Member> findByCredential(String accessToken);

    @Modifying
    @Transactional
    @Query(value = "DELETE FROM member WHERE member_id = :memberId", nativeQuery = true)
    int deleteAllByMember(@Param(value="memberId") Long memberId);
}

