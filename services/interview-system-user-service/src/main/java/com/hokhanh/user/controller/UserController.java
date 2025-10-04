package com.hokhanh.user.controller;

import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import com.hokhanh.common.user.request.CreateUserInput;
import com.hokhanh.common.user.response.CreateUserPayload;
import com.hokhanh.common.user.response.UserByEmailPayload;
import com.hokhanh.user.service.UserService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class UserController {
	private final UserService service;
	
	@MutationMapping
	public CreateUserPayload createUserInternal(@Argument @Valid CreateUserInput input) {
		return service.createUser(input);
	}
	
	@QueryMapping
	public UserByEmailPayload userByEmailInternal(@Argument String email) {
		return service.userByEmail(email);
	}
}
