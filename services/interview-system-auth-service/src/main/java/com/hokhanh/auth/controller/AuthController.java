package com.hokhanh.auth.controller;

import java.util.List;

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
import com.hokhanh.auth.response.common.BaseAuthPayload;
import com.hokhanh.auth.response.logout.LogoutApiPayload;
import com.hokhanh.auth.response.refreshToken.RefreshTokenApiPayload;
import com.hokhanh.auth.response.signin.SigninApiPayload;
import com.hokhanh.auth.response.signup.SignupCandidateApiPayload;
import com.hokhanh.auth.response.signup.SignupInterviewerApiPayload;
import com.hokhanh.auth.response.signup.VerifyInterviewerSignupOtpApiPayload;
import com.hokhanh.auth.service.AuthService;

import graphql.GraphQLContext;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Controller
@RequiredArgsConstructor
@Slf4j
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
		log.info("SIGNIN !!!");
		return authService.signin(input, context);
	}
	
	@MutationMapping
	public LogoutApiPayload logout(@Argument String refreshToken,
			@ContextValue(name = AuthenticationConstants.AUTHORIZATION_CONTEXT_KEY, required = false) String authorization,
			GraphQLContext context) {
		log.info("LOGOUT !!!");
		if(refreshToken == null || refreshToken.isBlank() || authorization == null || authorization.isBlank()) {
			return new LogoutApiPayload(new BaseApiPayload(false, "Missing refreshToken or authorization"));
		}
		
		String accessToken = authorization.replace("Bearer ", "").trim();
		
		return authService.logout(accessToken, refreshToken, context);
	}
	
	@MutationMapping
	public RefreshTokenApiPayload refreshToken(@Argument String refreshToken,
			GraphQLContext context) {
		System.out.println("rf: "+ refreshToken);
		return authService.refreshToken(refreshToken, context);
	}
	
	@QueryMapping
	public List<BaseAuthPayload> authsByUserId(@Argument @NotNull List<String> userIds) {
		return authService.authsByUserId(userIds);
	}
}
