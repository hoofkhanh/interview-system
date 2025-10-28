package com.hokhanh.question.request.questions;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;

public record BasePageInput(
	@NotNull(message = "page is required")
	@PositiveOrZero(message = "page is positive number or zero one")
	Integer page,
	
	@NotNull(message = "size is required")
	@Positive(message = "size is positive number")
	Integer size
) {

}
