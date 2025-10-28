package com.hokhanh.session.response.sharedFile.createSharedFile;

import com.hokhanh.session.response.common.BaseApiPayload;
import com.hokhanh.session.response.common.CacheApiStatusType;

public record CreateSharedFileApiPayload(
	BaseApiPayload metadata,
	CacheApiStatusType status,
	CreateSharedFilePayload payload
) {

}
