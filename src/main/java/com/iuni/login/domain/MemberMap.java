package com.iuni.login.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter
@Setter
public class MemberMap {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="name")
    private String name;

    @Column(name="description")
    private String description;

    @Column(name="type")
    private String type;

    @ManyToOne(fetch= FetchType.LAZY)
    @JoinColumn(name= "my_id")
    @ToString.Exclude
    private Member Mine;

    @ManyToOne(fetch= FetchType.LAZY)
    @JoinColumn(name= "friend_id")
    @ToString.Exclude
    private Member Friend;
}
