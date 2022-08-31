package com.pentagon.warungkita.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UsersResponsePOST {
    private String username;
    private String email;

    @Override
    public String toString() {
        return "UsersResponsePOST{" +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}
