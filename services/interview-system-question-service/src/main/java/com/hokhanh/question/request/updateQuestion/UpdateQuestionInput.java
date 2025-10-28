package com.hokhanh.question.request.updateQuestion;


import com.hokhanh.question.request.common.BaseQuestionInput;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record UpdateQuestionInput(
	@NotBlank(message = "id is required")
	String id,
	
	@Valid
	@NotNull(message = "baseQuestion must not be null")
	BaseQuestionInput baseQuestion
) {

}
