package com.pentagon.warungkita.controller;

import com.pentagon.warungkita.dto.PassworRequest;
import com.pentagon.warungkita.dto.PhotoRequestDTO;
import com.pentagon.warungkita.dto.UsersRequestDTO;
import com.pentagon.warungkita.service.PhotoProfileService;
import com.pentagon.warungkita.service.UsersService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
@RestController
@AllArgsConstructor
@Slf4j
@RequestMapping("/pentagon/warung-kita")
@SecurityRequirement(name = "bearer-key")
@Tag(name = "02.Users")
public class UsersController {

    @Autowired
    UsersService usersService;
    private final PhotoProfileService photoProfileService;

    @Operation(summary = "Upload Photo Profile (ADMIN, BUYER, SELLER)")
    @PostMapping(value = "/photo/add/profile",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasAuthority('ROLE_ADMIN')or hasAuthority('ROLE_SELLER')or hasAuthority('ROLE_BUYER')")
    public ResponseEntity<Object> createPhoto(@RequestPart PhotoRequestDTO photoRequestDTO, @RequestParam("file") MultipartFile multipartFile){

        return this.photoProfileService.createPhoto(photoRequestDTO, multipartFile);
    }

    @Operation(summary = "View all User (ADMIN)")
    @GetMapping("/users/all")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity <Object> getAll() {
        return usersService.getAll();
    }

    @Operation(summary = "View User Details (ADMIN, BUYER, SELLER)")
    @GetMapping("/users/user_details")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')or hasAuthority('ROLE_SELLER')or hasAuthority('ROLE_BUYER')")
    public ResponseEntity<Object> getUserById() {
        return usersService.userDetail();
    }

    @Operation(summary = "Complete User Details (BUYER, SELLER)")
    @PutMapping("/users/complete_profile")
    @PreAuthorize("hasAuthority('ROLE_SELLER')or hasAuthority('ROLE_BUYER')")
    public ResponseEntity<Object> completeProfile(@RequestBody UsersRequestDTO usersRequestDTO){
        return usersService.completeUsers(usersRequestDTO);
    }

    @Operation(summary = "Deactive Users (ADMIN)")
    @DeleteMapping("/users/delete_user/{users_Id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<Object> deactiveUser(@PathVariable Long users_Id){
       return usersService.deleteUserById(users_Id);
    }

    @Operation(summary = "Update User Details (ADMIN, BUYER, SELLER)")
    @PutMapping("/users")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')or hasAuthority('ROLE_SELLER')or hasAuthority('ROLE_BUYER')")
    public ResponseEntity<Object> updateUser(@RequestBody UsersRequestDTO usersRequestDTO){
        return usersService.update(usersRequestDTO);
    }

    @Operation(summary = "Update User to Seller (BUYER)")
    @PutMapping("/become_seller")
    @PreAuthorize("hasAuthority('ROLE_BUYER')")
    public ResponseEntity<Object> becomeSeller() {
        return usersService.becameSeller();
    }

    @Operation(summary = "Create new user (ADMIN)")
    @PostMapping("/users")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<Object> createUser(@RequestBody UsersRequestDTO usersRequestDTO) {
        return this.usersService.createUser(usersRequestDTO);
    }

    @Operation(summary = "Change Password (ADMIN, BUYER, SELLER)")
    @PutMapping("/users/change_password")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')or hasAuthority('ROLE_SELLER')or hasAuthority('ROLE_BUYER')")
    public ResponseEntity<Object> changePassword(@RequestBody PassworRequest request){
        return usersService.changePassword(request);
    }

    @Operation(summary = "Deactive Users (ADMIN, BUYER, SELLER)")
    @DeleteMapping("/users/deactive_user")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')or hasAuthority('ROLE_SELLER')or hasAuthority('ROLE_BUYER')")
    public ResponseEntity<Object> deactiveUserLogin(){
        return usersService.deactiveUserById();
    }
}
