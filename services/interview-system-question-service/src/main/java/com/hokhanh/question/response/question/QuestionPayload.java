package com.hokhanh.question.response.question;

import java.util.List;

import com.hokhanh.question.response.common.BaseQuestionPayload;
import com.hokhanh.question.response.common.BaseTestCasePayload;

public record QuestionPayload(
	BaseQuestionPayload baseQuestion,
	List<BaseTestCasePayload> testCases
) {

}
