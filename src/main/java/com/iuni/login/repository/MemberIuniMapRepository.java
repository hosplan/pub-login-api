package com.iuni.login.repository;

import com.iuni.login.domain.Member;
import com.iuni.login.domain.MemberIuniMap;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

public interface MemberIuniMapRepository extends JpaRepository<MemberIuniMap, Long> {
    @Modifying
    @Transactional
    @Query(value = "DELETE FROM member_iuni_map WHERE member_id = :memberId", nativeQuery = true)
    int deleteAllByMember(@Param(value="memberId") Long memberId);
}
