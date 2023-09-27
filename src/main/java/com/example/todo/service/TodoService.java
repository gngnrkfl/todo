package com.example.todo.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.todo.medel.TodoEntity;
import com.example.todo.persistence.TodoRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class TodoService {
	@Autowired
	private TodoRepository repository;

	public List<TodoEntity> create(final TodoEntity entity) {
		// Validations
		validate(entity);
		repository.save(entity);
		return repository.findByUserId(entity.getUserId());
	}

	public List<TodoEntity> retrieve(final String userId) {
		return repository.findByUserId(userId);
	}

	public List<TodoEntity> update(final TodoEntity entity) {
		// Validations
		validate(entity);
		if (repository.existsById(entity.getId())) {
			repository.save(entity);
		} else
			throw new RuntimeException("Unkown id");
		return repository.findByUserId(entity.getUserId());
	}

	public List<TodoEntity> updateTodo(final TodoEntity entity) {
		// Validations
		validate(entity);

		// 테이블에서 id에 해당하는 데이타셋을 가져온다.
		final Optional<TodoEntity> original = repository.findById(entity.getId());

		// original에 담겨진 내용을 todo에 할당하고 title, done 값을 변경한다.
		original.ifPresent(todo -> {
			todo.setTitle(entity.getTitle());
			todo.setDone(entity.isDone());
			repository.save(todo);
		});

		return repository.findByUserId(entity.getUserId());
	}
	
	public List<TodoEntity> delete(final TodoEntity entity) {
		if(repository.existsById(entity.getId())) // id 중복 확
			repository.deleteById(entity.getId());
		else 
			throw new RuntimeException("id does not exist");
		
		return repository.findByUserId(entity.getUserId());
	}

	public void validate(final TodoEntity entity) {
		if (entity == null) {
			log.warn("Entity cannot be null");
			throw new RuntimeException("Entity cnaanot be null");
		}
		if (entity.getUserId() == null) {
			log.warn("Unknown user");
			throw new RuntimeException("Unknown user");
		}
	}
}
