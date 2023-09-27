package com.example.todo.security;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

import org.springframework.stereotype.Service;

import com.example.todo.medel.UserEntity;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class TokenProvider {
	private static final String SECRET_KEY = "NMA8JPctFuna59f5";
	
	public String create(UserEntity userEntity) {
		// 토큰의 유효기간
		Date expireDate =Date.from(Instant.now().plus(1,ChronoUnit.DAYS));
		
		return Jwts.builder()
				.signWith(SignatureAlgorithm.HS512, SECRET_KEY) // header (시그니쳐의 암호화 알고리즘 명시)
				.setSubject(userEntity.getId()) // payload (관련 정보들)
				.setIssuer("todo app")
				.setIssuedAt(expireDate)
				.compact(); // signature
	}
	
	public String validateAndGetUserId(String token) {
		Claims claims = Jwts.parser()
				.setSigningKey(SECRET_KEY)
				.parseClaimsJws(token)
				.getBody();
		
		return claims.getSubject();
	}
}
