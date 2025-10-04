package com.hokhanh.auth.response.common;

import java.util.UUID;

public record BaseAuthPayload(
	Long id,
	RolePayload role,
	UUID userId,
	String emai,
	String fullName
) {

}
