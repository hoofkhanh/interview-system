package com.hokhanh.session.request.sharedFile.common;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record BaseFileInput(
	@NotBlank
	String sessionId,
	
	String folderId,
	
	@NotNull
	String content,
	
	@NotBlank
	String name
) {

}
