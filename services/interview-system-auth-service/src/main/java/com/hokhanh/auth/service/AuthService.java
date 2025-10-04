package com.hokhanh.auth.service;

import java.time.Duration;
import java.time.LocalDateTime;

import org.springframework.stereotype.Service;

import com.hokhanh.auth.client.user.UserClient;
import com.hokhanh.auth.email.AuthEmailService;
import com.hokhanh.auth.mapper.AuthMapper;
import com.hokhanh.auth.model.Role;
import com.hokhanh.auth.redis.JwtTokenCacheService;
import com.hokhanh.auth.redis.SignupInterviewCacheService;
import com.hokhanh.auth.repository.RoleRepository;
import com.hokhanh.auth.request.signup.SignupInterviewerInput;
import com.hokhanh.auth.request.signup.VerifyInterviewerSignupOtpInput;
import com.hokhanh.auth.response.common.BaseApiPayload;
import com.hokhanh.auth.response.signup.SignupApiStatusType;
import com.hokhanh.auth.response.signup.SignupInterviewerApiPayload;
import com.hokhanh.auth.response.signup.VerifyInterviewerSignupOtpApiPayload;
import com.hokhanh.auth.utils.OtpGenerator;
import com.hokhanh.common.user.response.UserByEmailPayload;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService {
	private final JwtTokenCacheService jwtTokenCacheService;
	
	private final UserClient userClient;
	
	private final RoleRepository roleRepo;
	
	private final SignupInterviewCacheService signupInterviewCacheService;
	
	private final AuthEmailService authEmailService;
	
	private final AuthMapper authMapper;
	

	public boolean isTokenBlocked(String accessToken) {
		return jwtTokenCacheService.isTokenInBlacklist(accessToken);
	}

	public SignupInterviewerApiPayload signupInterviewer(SignupInterviewerInput input) {
		UserByEmailPayload user =  userClient.userByEmailInternal(input.baseSignup().email());
		if(user != null && user.baseUser() != null) {
			return new SignupInterviewerApiPayload(
				new BaseApiPayload(false, "Email existing"),
				SignupApiStatusType.EMAIL_EXISTS,
				null
			);
		}
		
		Role role = roleRepo.findById(input.baseSignup().roleId()).orElse(null);
		if(role == null || !role.getName().equalsIgnoreCase("INTERVIEWER")) {
			return new SignupInterviewerApiPayload(
				new BaseApiPayload(false, "Role not existing"),
				SignupApiStatusType.ROLE_NOT_EXISTS,
				null
			);
		}
		
		String otp = OtpGenerator.generateOtp();
		long otpExpirationMinutes = 5;
		Duration ttl = Duration.ofMinutes(otpExpirationMinutes);
		signupInterviewCacheService.cacheSignupInterviewerInputAndOtp(input, otp, ttl);
		
		authEmailService.sendInterviewerSignupOtpToEmail(input.baseSignup().email(), otp, otpExpirationMinutes);
		
		return new SignupInterviewerApiPayload(
				new BaseApiPayload(true, "Signup successfully"),
				null,
				authMapper.toSignupInterviewerPayload(LocalDateTime.now(), otpExpirationMinutes)
		);
	}

	public VerifyInterviewerSignupOtpApiPayload verifyInterviewerSignupOtp(VerifyInterviewerSignupOtpInput input) {
		return null;
	}
}
