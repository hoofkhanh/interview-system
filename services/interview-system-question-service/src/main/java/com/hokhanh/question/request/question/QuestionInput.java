package com.hokhanh.question.request.question;

import jakarta.validation.constraints.NotBlank;

public record QuestionInput(
	@NotBlank(message = "id is required")
	String id		
) {

}
