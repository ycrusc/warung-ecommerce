package com.pentagon.warungkita.service;

import com.pentagon.warungkita.dto.PassworRequest;
import com.pentagon.warungkita.dto.UsersRequestDTO;
import com.pentagon.warungkita.model.Users;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;

public interface UsersService {

    ResponseEntity<Object> getAll();
    Users createUser(Users users);
    Optional<Users> getUserById(Long users_Id);
    Users findById(Long users_Id);
    Users updateUser(Users users) throws Exception;
    ResponseEntity<Object> deleteUserById(Long users_Id);
    Optional<Users> findByUsername(String username);
    ResponseEntity<Object> createUser(UsersRequestDTO usersRequestDTO);
    ResponseEntity<Object> changePassword(PassworRequest request);
    ResponseEntity<Object> completeUsers(UsersRequestDTO usersRequestDTO);
    ResponseEntity<Object> becameSeller();
    ResponseEntity<Object> userDetail();
    ResponseEntity<Object> deactiveUserById();
    ResponseEntity<Object> update(UsersRequestDTO requestDTO);
    ResponseEntity uploadUser(MultipartFile file) throws IOException;

    Workbook generateExcelReport();

}
