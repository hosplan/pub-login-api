package com.iuni.login.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
public class IuniStyle {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="background")
    private String background;

    @Column(name="body")
    private String body;

    @Column(name="leftWhiteEye")
    private String leftWhiteEye;

    @Column(name="leftEye")
    private String leftEye;

    @Column(name="nose")
    private String nose;

    @Column(name="rightWhiteEye")
    private String rightWhiteEye;

    @Column(name="rightEye")
    private String rightEye;

    @Column(name="hair")
    private String hair;

    @Column(name="leftEar")
    private String leftEar;

    @Column(name="rightEar")
    private String rightEar;

    @Column(name="rightFace")
    private String rightFace;
    @Column(name="leftFace")
    private String leftFace;
    @Column(name="type")
    private String type;

    @OneToMany(mappedBy = "iuniStyle", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JsonIgnore
    @ToString.Exclude
    private List<MemberIuniMap> memberIuniMaps = new ArrayList<>();
}
