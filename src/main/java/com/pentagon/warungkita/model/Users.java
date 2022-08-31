package com.pentagon.warungkita.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.pentagon.warungkita.dto.UsersResponseDTO;
import com.pentagon.warungkita.dto.UsersResponsePOST;
import lombok.*;

import javax.persistence.*;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
@Table(name = "users")
@Data
public class Users {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;
    private String email;
    private String fullName;
    private String username;
    private String password;
    private String address;
    @OneToOne
    @JoinColumn(name = "photo_id")
    private PhotoProfile profilPicture;

    private String phoneNum;
    private boolean active;


    @JsonIgnore
    @ManyToMany
    @JoinTable(name = "user_roles",
    joinColumns = @JoinColumn (name = "user_id"),
    inverseJoinColumns = @JoinColumn (name = "role_id" ))
    private List<Roles> roles;

    public Users(String username) {
        this.username = username;
    }

    public Users(String fullName, String username, String email, String password) {
        this.fullName = fullName;
        this.username = username;
        this.email = email;
        this.password = password;
    }

    public UsersResponseDTO convertToResponse(){
        return UsersResponseDTO.builder()
                .roles(this.roles)
                .fullName(this.fullName)
                .username(this.username)
                .email(this.email)
                .address(this.address)
                .phoneNum(this.phoneNum)
                .profilPicture(this.getProfilPicture())
                .build();
    }

    public UsersResponsePOST convertToResponsePOST(){
        return UsersResponsePOST.builder()
                .email(this.email)
                .username(this.username)
                .build();
    }

    @Override
    public String toString() {
        return "Users{" +
                "userId=" + userId +
                ", email='" + email + '\'' +
                ", fullName='" + fullName + '\'' +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", address='" + address + '\'' +
                ", profilPicture='" + profilPicture + '\'' +
                ", phoneNum='" + phoneNum + '\'' +
                ", active=" + active +
                ", roles=" + roles +
                '}';
    }
}
