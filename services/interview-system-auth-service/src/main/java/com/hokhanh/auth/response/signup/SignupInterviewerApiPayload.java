package com.hokhanh.auth.response.signup;

import com.hokhanh.auth.response.common.BaseApiPayload;

public record SignupInterviewerApiPayload(
	BaseApiPayload metadata,
	SignupApiStatusType status,
	SignupInterviewerPayload payload
) {

}
