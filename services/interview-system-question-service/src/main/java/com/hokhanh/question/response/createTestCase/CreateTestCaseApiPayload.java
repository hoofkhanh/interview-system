package com.hokhanh.question.response.createTestCase;

import com.hokhanh.question.response.common.BaseApiPayload;
import com.hokhanh.question.response.common.TestCaseApiStatusType;

public record CreateTestCaseApiPayload(
	BaseApiPayload metadata,
	TestCaseApiStatusType status,
	CreateTestCasePayload payload
) {

}
