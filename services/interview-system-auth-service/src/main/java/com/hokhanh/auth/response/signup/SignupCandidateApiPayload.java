package com.hokhanh.auth.response.signup;

import com.hokhanh.auth.response.common.BaseApiPayload;

public record SignupCandidateApiPayload(
	BaseApiPayload metadata,
	SignupApiStatusType status,
	SignupCandidatePayload payload
) {

}
