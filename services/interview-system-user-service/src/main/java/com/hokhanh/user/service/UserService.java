package com.hokhanh.user.service;

import org.springframework.stereotype.Service;

import com.hokhanh.common.user.request.CreateOrUpdateUserInput;
import com.hokhanh.common.user.response.CreateOrUpdateUserPayload;
import com.hokhanh.common.user.response.UserByEmailPayload;
import com.hokhanh.user.mapper.UserMapper;
import com.hokhanh.user.model.User;
import com.hokhanh.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;


@Service
@RequiredArgsConstructor
public class UserService {
	private final UserRepository repository;
	private final UserMapper mapper;

	public CreateOrUpdateUserPayload createOrUpdateUser( CreateOrUpdateUserInput input) {
		User user = repository.findByEmail(input.email());
		if(user != null) {
			mapper.updateUserFromInput(user, input);
		}else {
			user = mapper.toUser(input);
		}
		user = repository.save(user);
		return new CreateOrUpdateUserPayload(mapper.toBaseUserPayload(user));
	}

	public UserByEmailPayload userByEmail(String email) {
		User user = repository.findByEmail(email);
		return new UserByEmailPayload(mapper.toBaseUserPayload(user));
	}

}
