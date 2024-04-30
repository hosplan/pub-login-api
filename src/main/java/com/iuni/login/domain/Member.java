package com.iuni.login.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.iuni.login.helper.alarm.FromMember;
import com.iuni.login.helper.alarm.ToMember;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="member")
@Getter
@Setter
public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "email")
    private String email;
    @Column(name = "password", columnDefinition = "text")
    private String password;
    @Column(name = "isSocial")
    private Boolean isSocial;
    @Column(name = "nickName")
    private String nickName;
    @Column(name = "name")
    private String name;
    @Column(name = "salt", columnDefinition = "text")
    private String salt;
    @Column(name = "credential")
    private String credential;
    @Column(name = "refreshToken")
    private String refreshToken;
    @Column(name = "type")
    private String type;
    @Column(name = "certification")
    private Boolean certification;
    @Transient
    private String serialCode;
    @Transient
    private String authName;

    @OneToMany(mappedBy = "member", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @Builder.Default
    private List<Authority> roles = new ArrayList<>();

    @OneToMany(mappedBy = "toMember", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JsonIgnore
    @ToString.Exclude
    private List<Alarm> toMemberAlarms = new ArrayList<>();

    @OneToMany(mappedBy = "fromMember", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JsonIgnore
    @ToString.Exclude
    private List<Alarm> fromMemberAlarms = new ArrayList<>();

    @OneToMany(mappedBy = "member", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @Builder.Default
    private List<MemberIuniMap> memberIuniMaps = new ArrayList<>();

    public ToMember makeToMember(){
        ToMember result = new ToMember();
        result.setId(getId());
        result.setEmail(getEmail());
        result.setNickName(getNickName());
        return result;
    }

    public FromMember makeFromMember(){
        FromMember result= new FromMember();
        result.setId(getId());
        result.setEmail(getEmail());
        result.setNickName(getNickName());
        return result;
    }
}