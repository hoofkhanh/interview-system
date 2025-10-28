package com.hokhanh.question.request.updateTestCase;

import com.hokhanh.question.request.common.BaseTestCaseInput;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record UpdateTestCaseInput(
	@NotBlank(message =  "id is required")
	String id,
	
	@NotNull(message = "baseTestCase is required")
	@Valid
	BaseTestCaseInput baseTestCase
) {

}
