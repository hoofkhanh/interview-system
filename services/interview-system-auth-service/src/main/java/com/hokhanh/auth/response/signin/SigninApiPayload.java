package com.hokhanh.auth.response.signin;

import com.hokhanh.auth.response.common.BaseApiPayload;

public record SigninApiPayload(
	BaseApiPayload metadata,
	SigninApiStatusType status,
	SigninPayload payload
) {

}
