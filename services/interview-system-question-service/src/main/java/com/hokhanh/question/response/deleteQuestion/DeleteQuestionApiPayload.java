package com.hokhanh.question.response.deleteQuestion;

import com.hokhanh.question.response.common.BaseApiPayload;
import com.hokhanh.question.response.common.QuestionApiStatusType;

public record DeleteQuestionApiPayload(
	BaseApiPayload metadata,
	QuestionApiStatusType status,
	DeleteQuestionPayload payload
) {

}
