package com.example.todo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.todo.medel.UserEntity;
import com.example.todo.persistence.UserRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class UserService {
	@Autowired
	private UserRepository userRepository;
	
	public UserEntity create(final UserEntity userEntity) {
		if(userEntity == null || userEntity.getEmail() == null) {
			throw new RuntimeException("Invalid arguments"); // 예외처리
		}
		
		final String email = userEntity.getEmail();
		if(userRepository.existsByEmail(email)) { // 중복 확인
			log.warn("Email already exists {}",email);
			throw new RuntimeException("Email already exists");
		}
		
		return userRepository.save(userEntity);
	}
	
	public UserEntity getByCredentials(final String email, final String password) {
		return userRepository.findByEmailAndPassword(email, password);
	}
}
