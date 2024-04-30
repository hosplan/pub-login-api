package com.iuni.login.helper.alarm;

import com.iuni.login.domain.Member;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class ResAlarm {
    private Long id;
    private String name;
    private String description;
    private String alarmType;
    private String status;
    private Long relateId;
    private Date createDate;
    private Boolean isRead;
    private ToMember toMember;
    private FromMember fromMember;
}
