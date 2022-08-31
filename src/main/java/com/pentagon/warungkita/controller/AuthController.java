package com.pentagon.warungkita.controller;

import com.pentagon.warungkita.dto.*;
import com.pentagon.warungkita.dto.LoginRequest;
import com.pentagon.warungkita.dto.JwtResponse;
import com.pentagon.warungkita.security.service.AuthService;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@OpenAPIDefinition(info = @Info(title = "WarungKita",
        description = "Build by PENTAGON"))
@Tag(name ="01.Sign Up/Login")
public class AuthController {

    @Autowired
    AuthService authService;

    @Operation(summary = "Login for get the token access")
    @PostMapping("/login")
    public ResponseEntity<JwtResponse> authenticateUser(@RequestBody LoginRequest request) {
        return authService.authenticateUser(request);
        }

    @Operation(summary = "Sign Up for new User")
    @PostMapping("/signup")
    public ResponseEntity<Object> signup(@RequestBody SignupRequest request) {

        return authService.signup(request);
    }

}
