package com.hokhanh.question.response.questions;

import com.hokhanh.question.response.common.BaseApiPayload;
import com.hokhanh.question.response.common.QuestionApiStatusType;

public record QuestionsApiPayload(
	BaseApiPayload metadata,
	QuestionApiStatusType status,
	QuestionsPayload payload
) {

}
