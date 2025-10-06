package com.hokhanh.auth.controller;

import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.ContextValue;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import com.hokhanh.auth.constants.AuthenticationConstants;
import com.hokhanh.auth.request.signin.SigninInput;
import com.hokhanh.auth.request.signup.SignupCandidateInput;
import com.hokhanh.auth.request.signup.SignupInterviewerInput;
import com.hokhanh.auth.request.signup.VerifyInterviewerSignupOtpInput;
import com.hokhanh.auth.response.common.BaseApiPayload;
import com.hokhanh.auth.response.logout.LogoutApiPayload;
import com.hokhanh.auth.response.signin.SigninApiPayload;
import com.hokhanh.auth.response.signup.SignupCandidateApiPayload;
import com.hokhanh.auth.response.signup.SignupInterviewerApiPayload;
import com.hokhanh.auth.response.signup.VerifyInterviewerSignupOtpApiPayload;
import com.hokhanh.auth.service.AuthService;

import graphql.GraphQLContext;
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
	public VerifyInterviewerSignupOtpApiPayload verifyInterviewerSignupOtp(@Argument @Valid VerifyInterviewerSignupOtpInput input, GraphQLContext context) {
		return authService.verifyInterviewerSignupOtp(input, context);
	}
	
	@MutationMapping
	public SignupCandidateApiPayload signupCandidate(@Argument @Valid SignupCandidateInput input, GraphQLContext context) {
		return authService.signupCandidate(input, context);
	}
	
	@MutationMapping
	public SigninApiPayload signin(@Argument @Valid SigninInput input, GraphQLContext context) {
		return authService.signin(input, context);
	}
	
	@MutationMapping
	public LogoutApiPayload logout(@ContextValue(name = AuthenticationConstants.REFRESH_TOKEN_COOKIE_NAME) String refreshToken,
			@ContextValue(name = AuthenticationConstants.AUTHORIZATION_CONTEXT_KEY) String authorization,
			GraphQLContext context) {
		if(refreshToken == null || refreshToken.isBlank() || authorization == null || authorization.isBlank()) {
			return new LogoutApiPayload(new BaseApiPayload(false, "Missing refreshToken or authorization"));
		}
		
		String accessToken = authorization.replace("Bearer ", "").trim();
		
		return authService.logout(accessToken, refreshToken, context);
	}
}
