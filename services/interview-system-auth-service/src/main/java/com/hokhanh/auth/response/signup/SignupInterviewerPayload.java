package com.hokhanh.auth.response.signup;

import java.time.LocalDateTime;

public record SignupInterviewerPayload(
	LocalDateTime otpSentAt,
	Long otpExpirationMinutes
) {

}
