package com.iuni.login.repository;

import com.iuni.login.domain.IuniStyle;
import com.iuni.login.helper.join.JoinIuniStyle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface IuniStyleRepository extends JpaRepository<IuniStyle, Long> {
    @Query(value="SELECT i.id, i.background, i.body, i.nose, i.left_white_eye as leftWhiteEye, i.left_eye as leftEye, i.right_white_eye as rightWhiteEye, i.right_eye as rightEye " +
            "FROM iuni_style as i INNER JOIN member_iuni_map as m ON i.id = m.iuni_style_id " +
            "WHERE m.member_id = :memberId AND m.is_main= :isMain ", nativeQuery = true)
    JoinIuniStyle getByMemberId(@Param(value="memberId") Long MemberId, @Param(value="isMain") boolean isMain);

}
