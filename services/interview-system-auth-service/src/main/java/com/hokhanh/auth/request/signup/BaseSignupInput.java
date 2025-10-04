package com.hokhanh.auth.request.signup;

import java.time.LocalDate;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Positive;

public record BaseSignupInput(
	@NotNull(message = "roleId is required")
	@Positive(message = "roleId must be a positive number")
	Long roleId,
	
	@NotBlank(message = "email is required")
	@Email(message = "email must be in correct format")
	String email,
	
	@NotBlank(message = "phoneNumber is required")
	String phoneNumber,
	
	@NotNull(message = "gender is required")
	Boolean gender,
	
	@NotBlank(message = "firstName is required")
	String firstName,
	
	@NotBlank(message = "lastName is required")
	String lastName,
	
	@NotBlank(message = "fullName is required")
	String fullName,
	
	@NotNull(message = "dateOfBirth is required")
	@Past(message = "dateOfBirth must be before today")
	LocalDate dateOfBirth
) {

}
