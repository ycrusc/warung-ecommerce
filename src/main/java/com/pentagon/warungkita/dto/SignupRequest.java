package com.pentagon.warungkita.dto;

import java.io.Serializable;

import lombok.Data;

@Data
public class SignupRequest implements Serializable{

    private String username;
    private String password;
    private String email;
    private String fullName;

}

