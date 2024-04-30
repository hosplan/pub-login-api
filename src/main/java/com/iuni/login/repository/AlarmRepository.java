package com.iuni.login.repository;

import com.iuni.login.domain.Alarm;
import com.iuni.login.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.ArrayList;
import java.util.Optional;

public interface AlarmRepository extends JpaRepository<Alarm, Long> {
    Optional<ArrayList<Alarm>> findAllByToMemberAndIsRead(Member toMember, Boolean isRead);

    Optional<ArrayList<Alarm>> findByToMemberAndRelateIdAndAlarmType(Member toMember, Long relateId, String alarmType);
    Optional<ArrayList<Alarm>> findAllByToMember(Member member);
}
