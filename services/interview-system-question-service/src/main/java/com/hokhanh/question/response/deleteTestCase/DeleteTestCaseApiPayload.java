package com.hokhanh.question.response.deleteTestCase;

import com.hokhanh.question.response.common.BaseApiPayload;
import com.hokhanh.question.response.common.TestCaseApiStatusType;

public record DeleteTestCaseApiPayload(
	BaseApiPayload metadata,
	TestCaseApiStatusType status,
	DeleteTestCasePayload payload
) {

}
