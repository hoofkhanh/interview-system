package com.hokhanh.question.request.createQuestion;

import com.hokhanh.question.request.common.BaseQuestionInput;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

public record CreateQuestionInput(
	@Valid
	@NotNull(message = "baseQuestion must not be null")
	BaseQuestionInput baseQuestion
) {

}
