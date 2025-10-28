package com.hokhanh.question.request.deleteQuestion;

import jakarta.validation.constraints.NotBlank;

public record DeleteQuestionInput(
	@NotBlank(message = "id is required")
	String id
) {

}
