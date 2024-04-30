package com.iuni.login.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;


@Entity
@Getter
@Setter
public class MemberIuniMap {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch= FetchType.LAZY)
    @JoinColumn(name="member_id")
    @JsonIgnore
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "iuni_style_id")
    @JsonIgnore
    private IuniStyle iuniStyle;

    @Column(name="is_main", columnDefinition = "boolean default true")
    private Boolean isMain;

    @Transient
    private Long memberId;
}
