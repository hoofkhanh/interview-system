package com.hokhanh.auth.controller;

import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import com.hokhanh.auth.request.signup.SignupInterviewerInput;
import com.hokhanh.auth.request.signup.VerifyInterviewerSignupOtpInput;
import com.hokhanh.auth.response.signup.SignupInterviewerApiPayload;
import com.hokhanh.auth.response.signup.VerifyInterviewerSignupOtpApiPayload;
import com.hokhanh.auth.service.AuthService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class AuthController {
	private final AuthService authService;
	
	@QueryMapping
	public boolean isTokenBlockedInternal(@Argument String accessToken) {
		return authService.isTokenBlocked(accessToken);
	}
	
	@MutationMapping
	public SignupInterviewerApiPayload signupInterviewer(@Argument @Valid SignupInterviewerInput input) {
		return authService.signupInterviewer(input);
	}
	
	@MutationMapping
	public VerifyInterviewerSignupOtpApiPayload verifyInterviewerSignupOtp(@Argument @Valid VerifyInterviewerSignupOtpInput input) {
		return authService.verifyInterviewerSignupOtp(input);
	}
}
