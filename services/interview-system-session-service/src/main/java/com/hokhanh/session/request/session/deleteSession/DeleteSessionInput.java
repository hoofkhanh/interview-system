package com.hokhanh.session.request.session.deleteSession;

import jakarta.validation.constraints.NotBlank;

public record DeleteSessionInput(
	@NotBlank(message = "id is required")
	String id
) {

}
