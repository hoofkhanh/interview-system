package com.hokhanh.question.request.deleteTestCase;

import jakarta.validation.constraints.NotBlank;

public record DeleteTestCaseInput(
	@NotBlank(message =  "id is required")
	String id
) {

}
