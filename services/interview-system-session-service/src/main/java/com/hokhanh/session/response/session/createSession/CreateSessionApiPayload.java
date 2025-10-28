package com.hokhanh.session.response.session.createSession;

import com.hokhanh.session.response.common.BaseApiPayload;
import com.hokhanh.session.response.session.common.SessionApiStatusType;

public record CreateSessionApiPayload(
	BaseApiPayload metadata,
	SessionApiStatusType status,
	CreateSessionPayload payload
) {

}
