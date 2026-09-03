package com.jinsol.stockmate.domain.user.service;

import com.jinsol.stockmate.domain.user.dto.TokenResponse;
import com.jinsol.stockmate.domain.user.dto.UserLoginRequest;
import com.jinsol.stockmate.domain.user.dto.UserResponse;
import com.jinsol.stockmate.domain.user.dto.UserSignupRequest;
import com.jinsol.stockmate.domain.user.entity.User;
import com.jinsol.stockmate.domain.user.enums.Role;
import com.jinsol.stockmate.domain.user.repository.UserRepository;
import com.jinsol.stockmate.global.exception.DuplicateEmailException;
import com.jinsol.stockmate.global.jwt.JwtProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;

    @Transactional
    public UserResponse signup(UserSignupRequest request){
        if(userRepository.existsByEmail(request.getEmail())){
            throw new DuplicateEmailException("이미 가입된 이메일입니다.");
        }

        String encodedPassword = passwordEncoder.encode(request.getPassword());

        User user = User.builder().email(request.getEmail())
                .password(encodedPassword)
                .name(request.getName())
                .role(Role.ADMIN)
                .build();
        User savedUser = userRepository.save(user);
        return new UserResponse(savedUser);
    }

    // 로그인
    public TokenResponse login(UserLoginRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("이메일 또는 비밀번호가 일치하지 않습니다."));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new IllegalArgumentException("이메일 또는 비밀번호가 일치하지 않습니다.");
        }

        String token = jwtProvider.createToken(user.getEmail());
        return new TokenResponse(token);
    }
}
