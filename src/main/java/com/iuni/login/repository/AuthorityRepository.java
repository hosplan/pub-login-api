package com.iuni.login.repository;

import com.iuni.login.domain.Authority;
import com.iuni.login.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public interface AuthorityRepository extends JpaRepository<Authority, Long> {
    Optional<Authority> findByName(String name);

    @Modifying
    @Transactional
    @Query(value = "DELETE FROM authority WHERE member = :memberId", nativeQuery = true)
    int deleteAllByMember(@Param(value="memberId") Long memberId);
}
