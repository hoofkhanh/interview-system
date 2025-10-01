package com.hokhanh.service;

import org.springframework.stereotype.Service;

import com.hokhanh.mapper.UserMapper;
import com.hokhanh.model.User;
import com.hokhanh.repository.UserRepository;
import com.hokhanh.request.CreateUserInput;
import com.hokhanh.response.CreateUserPayload;
import com.hokhanh.response.UserByEmailPayload;

import lombok.RequiredArgsConstructor;


@Service
@RequiredArgsConstructor
public class UserService {
	private final UserRepository repository;
	private final UserMapper mapper;

	public CreateUserPayload createUser( CreateUserInput input) {
		User user = repository.save(mapper.toUser(input));
		return new CreateUserPayload(mapper.toBaseUserPayload(user));
	}

	public UserByEmailPayload userByEmail(String email) {
		User user = repository.findByEmail(email);
		return new UserByEmailPayload(mapper.toBaseUserPayload(user));
	}

}
