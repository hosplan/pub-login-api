package com.iuni.login.domain;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.iuni.login.helper.alarm.ToMember;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.Date;

@Entity
@Table(name="alarm")
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
public class Alarm {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="name")
    private String name;

    @Column(name="description")
    private String description;

    //어떤 타입인지(태스크, 프로젝트, 보드)
    @Column(name="alarm_type")
    private String alarmType;
    //어떤 상태인지
    @Column(name="status")
    private String status;

    @Column(name="relateId")
    private Long relateId;

    @Column(name="createDate")
    private Date createDate;

    @Column(name="isRead")
    private Boolean isRead;

    @JoinColumn(name = "toMember")
    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnore
    private Member toMember;

    @JoinColumn(name = "fromMember")
    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnore
    private Member fromMember;

    @Transient
    private Long memberId;

    @Transient
    private Long fromId;

}
