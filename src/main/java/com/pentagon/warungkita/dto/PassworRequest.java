package com.pentagon.warungkita.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PassworRequest {

    private String confirmUsername;

    private String password;

    private String confirmPassword;
}
