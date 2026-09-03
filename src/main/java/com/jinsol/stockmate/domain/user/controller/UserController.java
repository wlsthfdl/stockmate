package com.jinsol.stockmate.domain.user.controller;

import com.jinsol.stockmate.domain.user.dto.TokenResponse;
import com.jinsol.stockmate.domain.user.dto.UserLoginRequest;
import com.jinsol.stockmate.domain.user.dto.UserResponse;
import com.jinsol.stockmate.domain.user.dto.UserSignupRequest;
import com.jinsol.stockmate.domain.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    //회원가입
    @PostMapping("/signup")
    public ResponseEntity<UserResponse> signup(@Valid @RequestBody UserSignupRequest request){
        UserResponse response = userService.signup(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    //로그인
    @PostMapping("/login")
    public ResponseEntity<TokenResponse> login(@Valid @RequestBody UserLoginRequest request) {
        TokenResponse response = userService.login(request);
        return ResponseEntity.ok(response);
    }
}
