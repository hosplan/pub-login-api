package com.iuni.login.repository;

import com.iuni.login.domain.Alarm;
import com.iuni.login.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Optional;

public interface AlarmRepository extends JpaRepository<Alarm, Long> {
    Optional<ArrayList<Alarm>> findAllByToMemberAndIsRead(Member toMember, Boolean isRead);

    Optional<ArrayList<Alarm>> findByToMemberAndRelateIdAndAlarmType(Member toMember, Long relateId, String alarmType);
    Optional<ArrayList<Alarm>> findAllByToMember(Member member);

    @Modifying
    @Transactional
    @Query(value = "DELETE FROM alarm WHERE to_member = :memberId or from_member = :memberId", nativeQuery = true)
    int deleteAllByToMemberOrFromMember(@Param(value="memberId") Long memberId);

}
