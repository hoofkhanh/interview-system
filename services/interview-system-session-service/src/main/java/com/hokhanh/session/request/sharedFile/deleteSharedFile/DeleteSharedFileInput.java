package com.hokhanh.session.request.sharedFile.deleteSharedFile;

import jakarta.validation.constraints.NotBlank;

public record DeleteSharedFileInput(
	@NotBlank
	String sessionId,
	
	@NotBlank
	String id
) {

}
