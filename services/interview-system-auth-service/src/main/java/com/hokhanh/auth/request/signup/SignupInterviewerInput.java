package com.hokhanh.auth.request.signup;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record SignupInterviewerInput(
	@Valid
	@NotNull(message = "baseSignup must not be null")
	BaseSignupInput baseSignup,
	
	@NotBlank(message = "username is required")
	String username,
	
	@NotBlank(message = "password is required")
	String password
) {

}
