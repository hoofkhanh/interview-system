package com.hokhanh.question.response.common;

import java.util.UUID;

public record BaseTestCasePayload(
	UUID id,
	String input,
	String output,
	Boolean isHidden
) {

}
