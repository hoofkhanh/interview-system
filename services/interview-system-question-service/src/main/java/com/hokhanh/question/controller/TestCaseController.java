package com.hokhanh.question.controller;

import java.util.UUID;

import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.ContextValue;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.stereotype.Controller;

import com.hokhanh.common.httpHeader.HttpHeadersConstants;
import com.hokhanh.question.request.createTestCase.CreateTestCaseInput;
import com.hokhanh.question.request.deleteTestCase.DeleteTestCaseInput;
import com.hokhanh.question.request.updateTestCase.UpdateTestCaseInput;
import com.hokhanh.question.response.createTestCase.CreateTestCaseApiPayload;
import com.hokhanh.question.response.deleteTestCase.DeleteTestCaseApiPayload;
import com.hokhanh.question.response.updateTestCase.UpdateTestCaseApiPayload;
import com.hokhanh.question.service.TestCaseService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class TestCaseController {
	private final TestCaseService service;
	
	@MutationMapping
	public CreateTestCaseApiPayload createTestCase(@Argument @Valid CreateTestCaseInput input,
			@ContextValue(name = HttpHeadersConstants.HEADER_USER_ID) UUID userId) {
		return service.createTestCase(input, userId);
	}
	
	@MutationMapping
	public UpdateTestCaseApiPayload updateTestCase(@Argument @Valid UpdateTestCaseInput input,
			@ContextValue(name = HttpHeadersConstants.HEADER_USER_ID) UUID userId) {
		return service.updateTestCase(input, userId);
	}
	
	@MutationMapping
	public DeleteTestCaseApiPayload deleteTestCase(@Argument @Valid DeleteTestCaseInput input,
			@ContextValue(name = HttpHeadersConstants.HEADER_USER_ID) UUID userId) {
		return service.deleteTestCase(input, userId);
	}
}
