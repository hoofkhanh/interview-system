package com.hokhanh.auth.request.signup;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record VerifyInterviewerSignupOtpInput(
	@NotBlank(message = "otp is required")
	String otp,
	
	@NotBlank(message = "email is required")
	@Email(message = "email must be in correct format")
	String email
) {

}
