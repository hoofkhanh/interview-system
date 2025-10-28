package com.hokhanh.auth.response.signup;

import com.hokhanh.auth.response.common.BaseAuthPayload;

public record SignupCandidatePayload(
	BaseAuthPayload baseAuth,
	String accessToken,
	String refreshToken
) {

}
