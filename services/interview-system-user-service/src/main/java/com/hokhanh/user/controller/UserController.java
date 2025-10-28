package com.hokhanh.user.controller;

import java.util.List;

import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import com.hokhanh.common.user.request.CreateOrUpdateUserInput;
import com.hokhanh.common.user.response.CreateOrUpdateUserPayload;
import com.hokhanh.common.user.response.UserByEmailPayload;
import com.hokhanh.user.service.UserService;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class UserController {
	private final UserService service;
	
	@MutationMapping
	public CreateOrUpdateUserPayload createOrUpdateUserInternal(@Argument @Valid CreateOrUpdateUserInput input) {
		return service.createOrUpdateUser(input);
	}
	
	@QueryMapping
	public UserByEmailPayload userByEmailInternal(@Argument @NotBlank @Email String email) {
		return service.userByEmail(email);
	}
	
	@QueryMapping
	public List<UserByEmailPayload> usersByIdInternal(@Argument @NotNull List<String> ids) {
		return service.usersById(ids);
	}
}
