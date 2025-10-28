package com.hokhanh.question.response.createQuestion;

import com.hokhanh.question.response.common.BaseApiPayload;
import com.hokhanh.question.response.common.QuestionApiStatusType;

public record CreateQuestionApiPayload(
	BaseApiPayload metadata,
	QuestionApiStatusType status,
	CreateQuestionPayload payload
) {

}
