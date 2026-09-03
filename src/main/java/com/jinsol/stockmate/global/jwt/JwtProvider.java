package com.jinsol.stockmate.global.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

@Component
public class JwtProvider {
    private final SecretKey secretKey;
    private final long expiration;

    public JwtProvider(
            @Value("${jwt.secret}") String secret,
            @Value("${jwt.expiration}") long expiration
    ){
        //문자열로된 비밀키를 jwt라이브러리가 요구하는 암호화 키 객체로 변환하는 과정
        this.secretKey = Keys.hmacShaKeyFor(secret.getBytes());
        this.expiration = expiration;
    }

    //토큰 생성
    public String createToken(String email){
        Date now = new Date();
        Date expiryDate = new Date(now.getTime()+expiration);

        return Jwts.builder()
                .subject(email)
                .issuedAt(now)      //발급 시각
                .expiration(expiryDate)     //만료 시각
                .signWith(secretKey)        //비밀키 서명
                .compact();
    }

    //토큰에서 이메일 추출
    public String getEmail(String token){
        Claims claims = Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();
        return claims.getSubject();
    }

    //토큰 유효성 검증
    public boolean validateToken(String token){
       try{
           Jwts.parser()
                   .verifyWith(secretKey)
                   .build()
                   .parseSignedClaims(token);
           return true;
       }catch (Exception e){
           return false;
       }
    }
}
