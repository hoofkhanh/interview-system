package com.hokhanh.auth.request.signup;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

public record SignupCandidateInput(
	@Valid
	@NotNull(message = "baseSignup must not be null")
	BaseSignupInput baseSignup
) {

}
