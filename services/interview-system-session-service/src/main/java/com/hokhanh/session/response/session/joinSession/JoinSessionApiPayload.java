package com.hokhanh.session.response.session.joinSession;

import com.hokhanh.session.response.common.BaseApiPayload;
import com.hokhanh.session.response.session.common.SessionApiStatusType;

public record JoinSessionApiPayload(
	BaseApiPayload metadata,
	SessionApiStatusType status,
	JoinSessionPayload payload
) {

}
