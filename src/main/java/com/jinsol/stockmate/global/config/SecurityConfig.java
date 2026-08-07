package com.jinsol.stockmate.global.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

        //특정 http요청에 대해 웹 기반 보안 구성.
        //csrf: 공격자가 인증된 브라우저에 저장된 쿠키 세션정보를 활용하여 웹 서버에 사용자가 의도하지 않은 요청을 전달하는 것
        @Bean
        public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{
            http
                    .csrf(csrf -> csrf.disable())
                    .authorizeHttpRequests(auth -> auth
                            .anyRequest().permitAll()
                    );
            return http.build();
        }

        @Bean
        public PasswordEncoder passwordEncoder(){
            return new BCryptPasswordEncoder();
        }
}
