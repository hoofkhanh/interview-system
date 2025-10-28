package com.hokhanh.question.response.updateTestCase;

import com.hokhanh.question.response.common.BaseApiPayload;
import com.hokhanh.question.response.common.TestCaseApiStatusType;

public record UpdateTestCaseApiPayload(
	BaseApiPayload metadata,
	TestCaseApiStatusType status,
	UpdateTestCasePayload payload
) {

}
