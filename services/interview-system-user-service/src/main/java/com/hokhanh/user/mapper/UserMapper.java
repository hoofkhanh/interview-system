package com.hokhanh.user.mapper;

import org.springframework.stereotype.Service;

import com.hokhanh.common.user.request.CreateUserInput;
import com.hokhanh.common.user.response.BaseUserPayload;
import com.hokhanh.user.model.User;


@Service
public class UserMapper {
	
	public User toUser(CreateUserInput input) {
		return User.builder()
				.email(input.email())
				.phoneNumber(input.phoneNumber())
				.gender(input.gender())
				.firstName(input.firstName())
				.lastName(input.lastName())
				.fullName(input.fullName())
				.dateOfBirth(input.dateOfBirth())
				.build();
	}
	
	public BaseUserPayload toBaseUserPayload(User user) {
		if(user == null) return null;
		return new BaseUserPayload(
			user.getId(),
			user.getEmail(),
			user.getPhoneNumber(),
			user.getGender(),
			user.getFirstName(),
			user.getLastName(),
			user.getFullName(),
			user.getDateOfBirth()
		);
	}

}
