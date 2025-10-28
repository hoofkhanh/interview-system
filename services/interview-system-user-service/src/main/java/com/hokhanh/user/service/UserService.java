package com.hokhanh.user.service;

import java.util.List;
import java.util.UUID;

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

	public List<UserByEmailPayload> usersById(List<String> ids) {
		if (ids == null || ids.isEmpty()) {
		    return List.of();
		}

		List<UUID> uuidIds = ids.stream().map(i -> UUID.fromString(i)).toList();
		List<User> users = repository.findAllById(uuidIds);
		List<UserByEmailPayload> usersMapped = users.stream().map(u -> new UserByEmailPayload(mapper.toBaseUserPayload(u))).toList();
		return usersMapped;
	}

}
