package com.hokhanh.session.response.sharedFile.common;

import java.util.UUID;

public record BaseFilePayload(
	UUID id,
	UUID folderId,
	String content,
	String name		
) {

}
