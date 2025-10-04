package com.hokhanh.auth.response.signup;

import com.hokhanh.auth.response.common.BaseAuthPayload;

public record VerifyInterviewerSignupOtpPayload(
	BaseAuthPayload baseAuth,
	String accessToken
) {

}
