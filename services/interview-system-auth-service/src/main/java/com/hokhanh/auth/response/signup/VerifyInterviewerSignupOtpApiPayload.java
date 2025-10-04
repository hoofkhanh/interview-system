package com.hokhanh.auth.response.signup;

import com.hokhanh.auth.response.common.BaseApiPayload;

public record VerifyInterviewerSignupOtpApiPayload(
	BaseApiPayload metadata,
	VerifyOtpApiStatusType status,
	VerifyInterviewerSignupOtpPayload payload
) {

}
