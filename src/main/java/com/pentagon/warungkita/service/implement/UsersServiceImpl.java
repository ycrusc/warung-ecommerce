package com.pentagon.warungkita.service.implement;

import com.pentagon.warungkita.dto.PassworRequest;
import com.pentagon.warungkita.dto.UsersRequestDTO;
import com.pentagon.warungkita.dto.UsersResponseDTO;
import com.pentagon.warungkita.dto.UsersResponsePOST;
import com.pentagon.warungkita.exception.ResourceNotFoundException;
import com.pentagon.warungkita.model.Roles;
import com.pentagon.warungkita.model.Users;
import com.pentagon.warungkita.repository.RolesRepo;
import com.pentagon.warungkita.repository.UsersRepo;
import com.pentagon.warungkita.response.CommonResponse;
import com.pentagon.warungkita.response.ResponseHandler;
import com.pentagon.warungkita.security.service.UserDetailsImpl;
import com.pentagon.warungkita.service.UsersService;
import com.pentagon.warungkita.util.PoiUtils;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.util.*;

@Service
@AllArgsConstructor
@Slf4j
public class UsersServiceImpl implements UsersService {

    @Autowired
    private final UsersRepo usersRepo;
    private final RolesRepo rolesRepo;
    private final PasswordEncoder passwordEncoder;
    private static final Logger logger = LogManager.getLogger(UsersServiceImpl.class);

    @Override
    public Users findById(Long users_Id) {
        return usersRepo.findById(users_Id)
                .orElseThrow(() -> new ResourceNotFoundException("Pengguna dengan id " + users_Id + " tidak ditemukan"));

    }

    @Override
    public ResponseEntity<Object> getAll() {
        List<Users> users = usersRepo.findAll();
        if (users.isEmpty()) {
            throw new ResourceNotFoundException("User not exist");
        }
        try {
            List<Users> result = usersRepo.findAll();
            List<UsersResponseDTO> usersResponseDTOList = new ArrayList<>();
            logger.info("==================== Logger Start Get All User ====================");
            for (Users dataresult : result) {
                Map<String, Object> order = new HashMap<>();
                order.put("role            : ", dataresult.getRoles());
                order.put("id_akun         : ", dataresult.getUserId());
                order.put("nama_lengkap    : ", dataresult.getFullName());
                order.put("nama            : ", dataresult.getUsername());
                order.put("email           : ", dataresult.getEmail());
                order.put("alamat          : ", dataresult.getAddress());
                order.put("sandi           : ", dataresult.getPassword());
                order.put("nomor_tlp       : ", dataresult.getPhoneNum());
                order.put("foto            : ", dataresult.getProfilPicture());

                logger.info("role          : " + dataresult.getRoles());
                logger.info("id_akun       : " + dataresult.getUserId());
                logger.info("nama_lengkap  : " + dataresult.getFullName());
                logger.info("nama          : " + dataresult.getUsername());
                logger.info("email         : " + dataresult.getEmail());
                logger.info("alamat        : " + dataresult.getAddress());
                logger.info("sandi         : " + dataresult.getPassword());
                logger.info("nomor_tlp     : " + dataresult.getPhoneNum());
                logger.info("foto          : " + dataresult.getProfilPicture());
                UsersResponseDTO usersResponseDTO = dataresult.convertToResponse();
                usersResponseDTOList.add(usersResponseDTO);

                logger.info("==================== Logger End Get All User ====================");
            }
            return ResponseHandler.generateResponse("Successfully Get All User!", HttpStatus.OK, usersResponseDTOList);
        } catch (Exception e) {
            logger.error("------------------------------------");
            logger.error(e.getMessage());
            logger.error("------------------------------------");
            return ResponseHandler.generateResponse(e.getMessage(), HttpStatus.MULTI_STATUS, "Bad Request!!");
        }
    }

    @Override
    public Users createUser(Users users) {
        /**
         * Logic add role buyer when creating new user
         * */
        if (users.getRoles() == null) {
            Roles role = rolesRepo.findByName("ROLE_BUYER");
            List<Roles> roles = new ArrayList<>();
            roles.add(role);
            users.setRoles(roles);
            users.setActive(true);
        }
        users.setPassword(passwordEncoder.encode(users.getPassword()));
        return this.usersRepo.save(users);
    }

    public Optional<Users> getUserById(Long users_Id) {
        Optional<Users> optionalUser = usersRepo.findById(users_Id);
        if (optionalUser.isEmpty()) {
            throw new ResourceNotFoundException("User not exist with id :" + users_Id);
        }
        return this.usersRepo.findById(users_Id);
    }

    @Override
    public Users updateUser(Users users) {
        Optional<Users> optionalUser = usersRepo.findById(users.getUserId());
        if (optionalUser.isEmpty()) {
            throw new ResourceNotFoundException("User not exist with id :" + users.getUserId());
        }


        return this.usersRepo.save(users);
    }

    @Override
    public ResponseEntity<Object> deleteUserById(Long users_Id) {
        try {
            Optional<Users> optionalUser = usersRepo.findById(users_Id);
            if (optionalUser.isEmpty()) {
                throw new ResourceNotFoundException("User not exist with id :" + users_Id);
            }
            Users users = usersRepo.getReferenceById(users_Id);
            users.setActive(false);
            Map<String, Boolean> response = new HashMap<>();
            usersRepo.save(users);
            response.put("Delete Status", Boolean.TRUE);
            logger.info("==================== Logger Start Hard Delete User By ID ====================");
            logger.info(response);
            logger.info("==================== Logger End Hard Delete User By ID =================");
            return ResponseHandler.generateResponse("Successfully Delete User! ", HttpStatus.OK, response);
        } catch (ResourceNotFoundException e) {
            logger.error("------------------------------------");
            logger.error(e.getMessage());
            logger.error("------------------------------------");
            return ResponseHandler.generateResponse(e.getMessage(), HttpStatus.NOT_FOUND, "Data Not Found!");
        }
    }

    @Override
    public Optional<Users> findByUsername(String username) {

        return this.usersRepo.findByUsername(username);
    }

    @Override
    public ResponseEntity<Object> createUser(UsersRequestDTO usersRequestDTO) {
        try {
            if (usersRepo.existsByUsername(usersRequestDTO.getUsername())) {
                throw new Exception("Username already taken!");
            }
            if (usersRepo.existsByEmail(usersRequestDTO.getEmail())) {
                throw new Exception("Email already in use!");
            }
            if (usersRequestDTO.getRoles() == null) {
                Roles role = rolesRepo.findByName("ROLE_BUYER");
                List<Roles> roles = new ArrayList<>();
                roles.add(role);
                usersRequestDTO.setRoles(roles);
                usersRequestDTO.setActive(true);
            }
            usersRequestDTO.setPassword(passwordEncoder.encode(usersRequestDTO.getPassword()));
            Users users = usersRequestDTO.convertToEntity();
            usersRepo.save(users);
            UsersResponsePOST userResult = users.convertToResponsePOST();
            logger.info("==================== Logger Start Create New User ====================");
            logger.info(userResult);
            logger.info("==================== Logger End Create New User =================");
            return ResponseHandler.generateResponse("Successfully Created User!", HttpStatus.CREATED, userResult);
        } catch (Exception e) {
            logger.error("------------------------------------");
            logger.error(e.getMessage());
            logger.error("------------------------------------");
            return ResponseHandler.generateResponse(e.getMessage(), HttpStatus.BAD_REQUEST, "Bad Request!!");
        }
    }

    @Override
    public ResponseEntity<Object> changePassword(PassworRequest request) {
        try {
            UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            Users user1 = this.findById(userDetails.getUserId());
            if (!Objects.equals(request.getConfirmUsername(), userDetails.getUsername())) {
                throw new ResourceNotFoundException("Username is wrong");
            }
            if (userDetails.getPassword().equals(request.getPassword())) {
                throw new ResourceNotFoundException("Password is same");
            }
            if (!Objects.equals(request.getPassword(), request.getConfirmPassword())) {
                throw new ResourceNotFoundException("Password is not match, try again");
            }
            if (passwordEncoder.matches(request.getPassword(), userDetails.getPassword())) {
                throw new ResourceNotFoundException("Password cannot be the same as the previous password");
            }
            user1.setPassword((passwordEncoder.encode(request.getPassword())));
            user1.setActive(true);
            Users updateUsers = this.updateUser(user1);
            UsersResponseDTO result = updateUsers.convertToResponse();
            logger.info("==================== Logger Start Update User By ID ====================");
            logger.info(result);
            logger.info("==================== Logger End Update User By ID =================");
            return ResponseHandler.generateResponse("Successfully Updated Password!", HttpStatus.OK, result);
        } catch (Exception e) {
            logger.error("------------------------------------");
            logger.error(e.getMessage());
            logger.error("------------------------------------");
            return ResponseHandler.generateResponse(e.getMessage(), HttpStatus.NOT_FOUND, "Bad Request!!");
        }
    }

    @Override
    public ResponseEntity<Object> completeUsers(UsersRequestDTO usersRequestDTO) {
        try {
            UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            Optional<Users> user1 = usersRepo.findById(userDetails.getUserId());
            Users users = usersRequestDTO.convertToEntity();
            users.setUserId(userDetails.getUserId());
            users.setEmail(userDetails.getEmail());
            users.setPassword(userDetails.getPassword());
            users.setUsername(userDetails.getUsername());
            users.setFullName(user1.get().getFullName());
            users.setRoles(user1.get().getRoles());
            users.setActive(true);

            Users updateUsers = usersRepo.save(users);
            UsersResponseDTO result = updateUsers.convertToResponse();
            logger.info("==================== Logger Start Update User By ID ====================");
            logger.info(result);
            logger.info("==================== Logger End Update User By ID =================");
            return ResponseHandler.generateResponse("Successfully Updated User!", HttpStatus.OK, result);
        } catch (Exception e) {
            logger.error("------------------------------------");
            logger.error(e.getMessage());
            logger.error("------------------------------------");
            return ResponseHandler.generateResponse(e.getMessage(), HttpStatus.NOT_FOUND, "Bad Request!!");
        }
    }

    @Override
    public ResponseEntity<Object> becameSeller() {
        try {
            UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            Users users = this.findById(userDetails.getUserId());
            users.setUserId(userDetails.getUserId());
            Roles role1 = rolesRepo.findByName("ROLE_SELLER");
            Roles role2 = rolesRepo.findByName("ROLE_BUYER");
            List<Roles> roles = new ArrayList<>();
            roles.add(role1);
            roles.add(role2);
            users.setRoles(roles);
            users.setActive(true);
            Users updateUsers = usersRepo.save(users);
            UsersResponseDTO result = updateUsers.convertToResponse();
            logger.info("==================== Logger Start Update User By ID ====================");
            logger.info(result);
            logger.info("==================== Logger End Update User By ID =================");
            return ResponseHandler.generateResponse("Successfully Open Shop", HttpStatus.CREATED, result);
        } catch (Exception e) {
            logger.error("------------------------------------");
            logger.error(e.getMessage());
            logger.error("------------------------------------");
            return ResponseHandler.generateResponse(e.getMessage(), HttpStatus.BAD_REQUEST, "Bad Request!!");
        }

    }

    @Override
    public ResponseEntity<Object> userDetail() {
        try {
            UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            Optional<Users> users = this.getUserById(userDetails.getUserId());
            Users userResult = users.get();
            UsersResponseDTO result = userResult.convertToResponse();
            logger.info("==================== Logger Start Get User By ID ====================");
            logger.info(result);
            logger.info("==================== Logger End Get User By ID =================");
            return ResponseHandler.generateResponse("Successfully Get User By ID!", HttpStatus.OK, result);
        } catch (Exception e) {
            logger.error("------------------------------------");
            logger.error(e.getMessage());
            logger.error("------------------------------------");
            return ResponseHandler.generateResponse(e.getMessage(), HttpStatus.NOT_FOUND, "Data Not Found!");
        }
    }

    @Override
    public ResponseEntity<Object> update(UsersRequestDTO requestDTO) {
        try {
            UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            Optional<Users> optionalUser = usersRepo.findById(userDetails.getUserId());
            Users users = requestDTO.convertToEntity();
            users.setUserId(userDetails.getUserId());
            users.setRoles(optionalUser.get().getRoles());
            users.setPassword(userDetails.getPassword());
            users.setActive(true);
            Users updateUsers = usersRepo.save(users);
            UsersResponseDTO result = updateUsers.convertToResponse();
            logger.info("==================== Logger Start Update User By ID ====================");
            logger.info(result);
            logger.info("==================== Logger End Update User By ID =================");
            return ResponseHandler.generateResponse("Successfully Updated User. If you change username, please login again.", HttpStatus.OK, result);
        } catch (Exception e) {
            logger.error("------------------------------------");
            logger.error(e.getMessage());
            logger.error("------------------------------------");
            return ResponseHandler.generateResponse(e.getMessage(), HttpStatus.NOT_FOUND, "Bad Request!!");
        }
    }


    @Override
    public ResponseEntity<Object> deactiveUserById() {
        try {
            UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            Users users = usersRepo.getReferenceById(userDetails.getUserId());
            users.setActive(false);
            Map<String, Boolean> response = new HashMap<>();
            usersRepo.save(users);
            response.put("Delete Status", Boolean.TRUE);
            logger.info("==================== Logger Start Hard Delete User By ID ====================");
            logger.info(response);
            logger.info("==================== Logger End Hard Delete User By ID =================");
            return ResponseHandler.generateResponse("Successfully Delete User! ", HttpStatus.OK, response);
        } catch (ResourceNotFoundException e) {
            logger.error("------------------------------------");
            logger.error(e.getMessage());
            logger.error("------------------------------------");
            return ResponseHandler.generateResponse(e.getMessage(), HttpStatus.NOT_FOUND, "Data Not Found!");
        }
    }

    @Override
    @Transactional(rollbackOn = {SQLException.class, IOException.class, IllegalArgumentException.class, Exception.class})
    public ResponseEntity uploadUser(MultipartFile file) throws IOException {
        FileInputStream fis = (FileInputStream) file.getInputStream();
        Workbook wb = new XSSFWorkbook(fis);
        Sheet sheet = wb.getSheetAt(0);
        List<Users> users = new ArrayList<>();

        boolean duplicate = false;
        List<String> userDuplicate = new ArrayList<>();
        for (Row row : sheet) {
            if (row.getRowNum() == 0) continue;
            Optional<Users> usersOptional = usersRepo.findByUsername(PoiUtils.cellValue(row.getCell(5)));
            if(usersOptional.isPresent()){
                duplicate = true;
                userDuplicate.add(PoiUtils.cellValue(row.getCell(5)));
                continue;
            }
            Users user = new Users();
            user.setAddress(PoiUtils.cellValue(row.getCell(0)));
            user.setEmail(PoiUtils.cellValue(row.getCell(1)));
            user.setFullName(PoiUtils.cellValue(row.getCell(2)));
            user.setPassword(passwordEncoder.encode(PoiUtils.cellValue(row.getCell(3))));
            user.setPhoneNum(PoiUtils.cellValue(row.getCell(4)));
            user.setUsername(PoiUtils.cellValue(row.getCell(5)));
            user.setActive(true);
            users.add(user);

        }
        if (duplicate) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    CommonResponse.builder()
                            .responseCode(HttpStatus.BAD_REQUEST.toString())
                            .data(userDuplicate)
                            .responseMessage("Upload data failed. Indicate duplicate data, please check your data")
                            .build()
            );
        }
        usersRepo.saveAll(users);
        return ResponseHandler.generateResponse("Successfully", HttpStatus.OK, users);
    }

    public Workbook generateExcelReport(){
        List<Users> users = usersRepo.findAll();

        Workbook wb = new XSSFWorkbook();
        Sheet sheet = wb.createSheet("User Data");

        setupHeader(wb, sheet);

        if(users != null && !users.isEmpty()){
            int rownum = 1;
            Row row = null;
            for(Users user : users){
                row = sheet.createRow(rownum);
                row.createCell(0).setCellValue(user.getUserId());
                row.createCell(1).setCellValue(user.getEmail());
                row.createCell(2).setCellValue(user.getFullName());
                row.createCell(3).setCellValue(user.getPhoneNum());
                row.createCell(4).setCellValue(user.getUsername());
                row.createCell(5).setCellValue(user.getAddress());
                rownum ++;
            }
        }
        return wb;
    }

    public void setupHeader(Workbook wb, Sheet sheet){

        Row header = sheet.createRow(0);
        CellStyle style = wb.createCellStyle();
        style.setFillForegroundColor(IndexedColors.LIGHT_BLUE.getIndex());
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);

        XSSFFont font = ((XSSFWorkbook)wb).createFont();
        font.setFontName("Arial");
        font.setFontHeightInPoints((short)16);
        font.setBold(true);
        style.setFont(font);

        Cell headerCell = header.createCell(0);
        headerCell.setCellValue("User Id");
        headerCell.setCellStyle(style);

        headerCell = header.createCell(1);
        headerCell.setCellValue("Email");
        headerCell.setCellStyle(style);

        headerCell = header.createCell(2);
        headerCell.setCellValue("Full Name");
        headerCell.setCellStyle(style);

        headerCell = header.createCell(3);
        headerCell.setCellValue("Phone Number");
        headerCell.setCellStyle(style);

        headerCell = header.createCell(4);
        headerCell.setCellValue("Username");
        headerCell.setCellStyle(style);

        headerCell = header.createCell(5);
        headerCell.setCellValue("Address");
        headerCell.setCellStyle(style);
    }
}

