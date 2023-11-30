package com.gonet.study001.domain;

import lombok.*;
import org.hibernate.annotations.Generated;

import javax.persistence.*;
import java.util.Date;


@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "tn_recruit")
public class Recruit {
    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String email;

    private Integer age;
    private String addr;
    private String school;
    private String photo;

    private String atchFile1;
    private String atchFile2;
    private String atchFile3;
    private String atchFile4;

    @Temporal(TemporalType.TIMESTAMP)
    private Date regDate;

    // 생성자에 @Builder 적용
    @Builder
    public Recruit(String name, String email, Integer age, String addr, String school, String photo, String atchFile1, String atchFile2) {
        this.name = name;
        this.email = email;
        this.age = age;
        this.addr = addr;
        this.school = school;
        this.photo = photo;
        this.atchFile1 = atchFile1;
        this.atchFile2 = atchFile2;
        this.regDate = new Date();
    }
}