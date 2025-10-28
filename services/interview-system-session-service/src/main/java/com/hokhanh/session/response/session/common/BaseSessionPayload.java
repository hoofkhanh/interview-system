package com.hokhanh.session.response.session.common;

import java.time.LocalDateTime;
import java.util.UUID;

import com.hokhanh.session.common.SessionStatusType;

public record BaseSessionPayload(
	UUID id,
	UUID creatorId,
	UUID questionId,
	String link,
	LocalDateTime startTime,
	SessionStatusType status,
	String title,
	String githubLink
) {

}
