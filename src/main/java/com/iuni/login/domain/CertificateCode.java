package com.iuni.login.domain;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CertificateCode {
    private String email;
    private String type;
    private String code;
}
