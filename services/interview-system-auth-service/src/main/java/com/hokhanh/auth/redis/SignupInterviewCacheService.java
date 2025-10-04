package com.hokhanh.auth.redis;

import java.time.Duration;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.hokhanh.auth.request.signup.SignupInterviewerInput;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SignupInterviewCacheService {

	private final RedisService redis;
	
	private final static String SIGNUP_INTERVIEWER_INPUT_OTP_REDIS_KEY = "signup_interviewer_input_otp:";
	
	
	public void cacheSignupInterviewerInputAndOtp(SignupInterviewerInput signupInterviewerInput, String otp, Duration ttl) {
		redis.set(SIGNUP_INTERVIEWER_INPUT_OTP_REDIS_KEY + signupInterviewerInput.baseSignup().email(),
			new SignupInterviewInputAndOtp(signupInterviewerInput, otp),
			ttl
		);
	}
	
	public SignupInterviewInputAndOtp getSignupInterviewerInputAndOtp(String email) {
		return redis.get(SIGNUP_INTERVIEWER_INPUT_OTP_REDIS_KEY + email, SignupInterviewInputAndOtp.class);
	}
	
	@Async
	public void deleteSignupInterviewerInputAndOtp(String email) {
		redis.delete(SIGNUP_INTERVIEWER_INPUT_OTP_REDIS_KEY + email);
	}
}
