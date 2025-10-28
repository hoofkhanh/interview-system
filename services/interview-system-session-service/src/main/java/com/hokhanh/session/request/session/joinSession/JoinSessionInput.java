package com.hokhanh.session.request.session.joinSession;

import jakarta.validation.constraints.NotBlank;

public record JoinSessionInput(
	@NotBlank
	String sessionId
) {

}
