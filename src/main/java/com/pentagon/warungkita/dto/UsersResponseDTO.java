package com.pentagon.warungkita.dto;

import com.pentagon.warungkita.model.Photo;
import com.pentagon.warungkita.model.PhotoProfile;
import com.pentagon.warungkita.model.Roles;
import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UsersResponseDTO {

    private List<Roles> roles;
    private String fullName;
    private String email;
    private String username;
    private String address;

    private PhotoProfile profilPicture;

    private String phoneNum;

    @Override
    public String toString() {
        return "UsersResponseDTO{" +
                "roles=" + roles +
                ", fullName='" + fullName + '\'' +
                ", email='" + email + '\'' +
                ", username='" + username + '\'' +
                ", address='" + address + '\'' +
                ", profilPicture='" + profilPicture + '\'' +
                ", phoneNum='" + phoneNum + '\'' +
                '}';
    }
}
