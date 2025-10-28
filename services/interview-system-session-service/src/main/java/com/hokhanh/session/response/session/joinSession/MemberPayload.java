package com.hokhanh.session.response.session.joinSession;

import java.util.UUID;

public record MemberPayload(
	UUID userId,
	String fullName,
	String roleName
) {

}
