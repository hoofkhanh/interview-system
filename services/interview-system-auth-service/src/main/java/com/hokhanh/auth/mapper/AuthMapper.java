package com.hokhanh.auth.mapper;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;

import com.hokhanh.auth.response.signup.SignupInterviewerPayload;

@Service
public class AuthMapper {

	public SignupInterviewerPayload toSignupInterviewerPayload(LocalDateTime otpSentAt, Long otpExpirationMinutes) {
		return new SignupInterviewerPayload(otpSentAt,otpExpirationMinutes);
	}
}
