package com.hokhanh.question.response.common;

import java.time.LocalDateTime;
import java.util.UUID;

public record BaseQuestionPayload(
	UUID id,
	UUID creatorId,
	String title,
	String description,
	LocalDateTime createdAt
) {

}
