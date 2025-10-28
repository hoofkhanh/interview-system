package com.hokhanh.session.response.session.deleteSession;

import com.hokhanh.session.response.common.BaseApiPayload;
import com.hokhanh.session.response.session.common.SessionApiStatusType;

public record DeleteSessionApiPayload(
	BaseApiPayload metadata,
	SessionApiStatusType status,
	DeleteSessionPayload payload
) {

}
