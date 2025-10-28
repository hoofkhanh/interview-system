package com.hokhanh.session.request.session.common;

import java.time.LocalDateTime;

import com.hokhanh.session.common.SessionStatusType;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record BaseSessionInput(
	@NotBlank(message = "questionId is required")
	String questionId,
	
	@NotNull(message = "startTime is required")
	@FutureOrPresent
	LocalDateTime startTime,
	
	@NotNull(message = "status is required")
	SessionStatusType status,
	
	@NotBlank(message = "title is required")
	String title,
	
	String githubLink
) {

}
