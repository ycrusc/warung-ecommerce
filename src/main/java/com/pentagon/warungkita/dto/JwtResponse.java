package com.pentagon.warungkita.dto;

import java.io.Serializable;
import java.util.List;

import lombok.Data;

@Data
public class JwtResponse implements Serializable {
    private String token;
    private String type = "Bearer";
    private String username;
    private String email;
    private List<String> roles;
    public JwtResponse(
            String accessToken,
            String username,
            String email,List<String> roles) {
        this.username = username;
        this.email = email;
        this.token = accessToken;
        this.roles = roles;
    }

}
