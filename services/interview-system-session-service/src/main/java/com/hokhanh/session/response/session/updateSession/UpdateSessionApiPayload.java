package com.hokhanh.session.response.session.updateSession;

import com.hokhanh.session.response.common.BaseApiPayload;
import com.hokhanh.session.response.session.common.SessionApiStatusType;

public record UpdateSessionApiPayload(
	BaseApiPayload metadata,
	SessionApiStatusType status,
	UpdateSessionPayload payload
) {

}
