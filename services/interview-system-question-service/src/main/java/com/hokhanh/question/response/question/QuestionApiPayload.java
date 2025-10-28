package com.hokhanh.question.response.question;

import com.hokhanh.question.response.common.BaseApiPayload;
import com.hokhanh.question.response.common.QuestionApiStatusType;

public record QuestionApiPayload(
	BaseApiPayload metadata,
	QuestionApiStatusType status,
	QuestionPayload payload
) {

}
