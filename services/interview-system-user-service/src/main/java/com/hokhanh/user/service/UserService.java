package com.hokhanh.user.service;

import org.springframework.stereotype.Service;

import com.hokhanh.user.mapper.UserMapper;
import com.hokhanh.user.model.User;
import com.hokhanh.user.repository.UserRepository;
import com.hokhanh.user.request.CreateUserInput;
import com.hokhanh.user.response.CreateUserPayload;
import com.hokhanh.user.response.UserByEmailPayload;

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
