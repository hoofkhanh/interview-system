package com.hokhanh.question.request.createTestCase;

import com.hokhanh.question.request.common.BaseTestCaseInput;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CreateTestCaseInput(
	@NotBlank(message = "questionId is required")
	String questionId,
	
	@Valid
	@NotNull(message = "baseTestCase must not be null")
	BaseTestCaseInput baseTestCase
) {

}
