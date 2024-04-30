package com.iuni.login.repository;

import com.iuni.login.domain.MemberMap;
import com.iuni.login.helper.join.JoinFriend;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.ArrayList;

public interface MemberMapRepository extends JpaRepository<MemberMap, Long> {

    @Query(value="select * from member", nativeQuery=true)
    ArrayList<JoinFriend> findAllMyFriend(@Param(value="id") Long id);
}
