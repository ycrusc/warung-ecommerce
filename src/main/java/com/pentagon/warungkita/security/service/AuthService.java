package com.pentagon.warungkita.security.service;

import com.pentagon.warungkita.dto.JwtResponse;
import com.pentagon.warungkita.dto.LoginRequest;
import com.pentagon.warungkita.dto.SignupRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;

public interface AuthService {
    ResponseEntity<Object> signup(@RequestBody SignupRequest request);
    ResponseEntity<JwtResponse> authenticateUser(@RequestBody LoginRequest request);
}
