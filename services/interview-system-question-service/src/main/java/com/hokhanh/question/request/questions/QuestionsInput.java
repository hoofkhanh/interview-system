package com.hokhanh.question.request.questions;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

public record QuestionsInput(
	@NotNull(message = "basePage must not be null")
	@Valid
	BasePageInput basePage,
	
	String keyword
) {

}
