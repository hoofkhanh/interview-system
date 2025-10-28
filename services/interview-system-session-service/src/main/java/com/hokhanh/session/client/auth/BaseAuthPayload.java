package com.hokhanh.session.client.auth;

import java.util.UUID;

public record BaseAuthPayload(
	UUID id,
	RolePayload role,
	UUID userId,
	String email,
	String fullName
) {

}
