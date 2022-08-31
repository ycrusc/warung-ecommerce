package com.pentagon.warungkita.security.service;

import com.pentagon.warungkita.controller.AuthController;
import com.pentagon.warungkita.dto.*;
import com.pentagon.warungkita.dto.LoginRequest;
import com.pentagon.warungkita.dto.JwtResponse;
import com.pentagon.warungkita.exception.ResourceNotFoundException;
import com.pentagon.warungkita.model.*;
import com.pentagon.warungkita.repository.UsersRepo;
import com.pentagon.warungkita.response.ResponseHandler;
import com.pentagon.warungkita.security.jwt.JwtUtils;
import com.pentagon.warungkita.service.UsersService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class AuthServiceImpl implements AuthService {

    @Autowired
    AuthenticationManager authenticationManager;
    @Autowired
    UsersRepo usersRepo;
    @Autowired
    UsersService usersService;
    @Autowired
    JwtUtils jwtUtils;

    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);
    @Override
    public ResponseEntity<Object> signup(SignupRequest request){

        try {
            if(request.getPassword().isEmpty() || request.getUsername().isEmpty() || request.getEmail().isEmpty() || request.getFullName().isEmpty()){
                throw new ResourceNotFoundException("Complete all field");
            }

            if (usersRepo.existsByUsername(request.getUsername())) {
                throw new Exception("Username already taken!");
            }
            if (usersRepo.existsByEmail(request.getEmail())) {
                throw new Exception("Email already in use!");
            }

            Users users = new Users();
            users.setUsername(request.getUsername());
            users.setEmail(request.getEmail());
            users.setPassword(request.getPassword());
            users.setFullName(request.getFullName());
            users.setActive(true);
            usersService.createUser(users);
            UsersResponsePOST userResult = users.convertToResponsePOST();
            logger.info("------------------------------------");
            logger.info("User created: " + userResult);
            logger.info("------------------------------------");
            return ResponseHandler.generateResponse("Successfully Created User!", HttpStatus.CREATED, userResult);
        } catch (Exception e) {
            logger.error("------------------------------------");
            logger.error(e.getMessage());
            logger.error("------------------------------------");
            return ResponseHandler.generateResponse(e.getMessage(), HttpStatus.BAD_REQUEST, "Sign Up failed");
        }
    }

    @Override
    public ResponseEntity<JwtResponse> authenticateUser(LoginRequest request) {
        if(request.getPassword().isEmpty() || request.getUsername().isEmpty()){
            throw new ResourceNotFoundException("Username and Password can't be null");
        }

        Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));
        Optional<Users> user = usersRepo.findByUsername(request.getUsername());
        if(user.get().isActive() == false){
            throw new ResourceNotFoundException("Username is not active, please contact admin.");
        }
        SecurityContextHolder.getContext()
                .setAuthentication(authentication);
        String token = jwtUtils.generateJwtToken(authentication);
        UserDetailsImpl principal = (UserDetailsImpl) authentication.getPrincipal();
        Optional<Users> users = usersService.findByUsername(principal.getUsername());
        List<Roles> roles = users.get().getRoles();
        List<String> stringsrole = new ArrayList<>();
        roles.forEach(roles1 -> {
            stringsrole.add(roles1.getName());
        });
        return ResponseEntity.ok().body(new JwtResponse(token, principal.getUsername(), principal.getEmail(), stringsrole));

    }
}