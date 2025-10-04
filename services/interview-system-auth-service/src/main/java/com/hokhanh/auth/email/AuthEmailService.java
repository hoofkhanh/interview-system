package com.hokhanh.auth.email;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthEmailService {
	private final EmailService emailService;

	@Async
	public void sendInterviewerSignupOtpToEmail(String email, String otp, long otpExpirationMinutes) {
		emailService.sendGmail(
	            email,
	            String.format("Your OTP for registration"),
	            String.format("Your OTP is valid for %d minutes and your OTP is %s", otpExpirationMinutes, otp)
	        );
	}
}
