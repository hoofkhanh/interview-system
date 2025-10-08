package com.hokhanh.auth.response.refreshToken;

import com.hokhanh.auth.response.common.BaseApiPayload;

public record RefreshTokenApiPayload(
	BaseApiPayload metadata,
	RefreshTokenApiStatusType status,
	RefreshTokenPayload payload
) {

}
