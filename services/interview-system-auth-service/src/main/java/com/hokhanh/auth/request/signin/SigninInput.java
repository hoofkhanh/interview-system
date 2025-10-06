package com.hokhanh.auth.request.signin;

import jakarta.validation.constraints.NotBlank;

public record SigninInput(
	@NotBlank(message = "username is required")
	String username,
	
	@NotBlank(message = "password is required")
	String password
) {

}
