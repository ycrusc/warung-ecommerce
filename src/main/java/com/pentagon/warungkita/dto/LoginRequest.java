package com.pentagon.warungkita.dto;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

@Data
public class LoginRequest implements Serializable {
    private String username;
    private String password;


}
