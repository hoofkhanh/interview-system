package com.hokhanh.question.request.common;

import jakarta.validation.constraints.NotBlank;

public record BaseQuestionInput(
	@NotBlank(message = "title is required")
	String title,
	
	@NotBlank(message = "description is required")
	String description
) {

}
