package com.hokhanh.auth.redis;

import com.hokhanh.auth.request.signup.SignupInterviewerInput;

public record SignupInterviewInputAndOtp(
	SignupInterviewerInput signupInterviewInput,
	String otp
) {

}
