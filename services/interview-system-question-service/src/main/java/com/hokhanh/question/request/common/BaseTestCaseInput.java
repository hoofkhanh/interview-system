package com.hokhanh.question.request.common;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record BaseTestCaseInput(
	@NotBlank(message = "input is required")
	String input,
	
	@NotBlank(message = "output is required")
	String output,
	
	@NotNull(message = "isHidden is required")
	Boolean isHidden
) {

}
