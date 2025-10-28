package com.hokhanh.question.response.updateQuestion;

import com.hokhanh.question.response.common.BaseApiPayload;
import com.hokhanh.question.response.common.QuestionApiStatusType;

public record UpdateQuestionApiPayload(
	BaseApiPayload metadata,
	QuestionApiStatusType status,
	UpdateQuestionPayload payload
) {

}
