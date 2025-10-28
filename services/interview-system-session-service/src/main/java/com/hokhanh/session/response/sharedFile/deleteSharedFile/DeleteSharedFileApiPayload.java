package com.hokhanh.session.response.sharedFile.deleteSharedFile;

import com.hokhanh.session.response.common.BaseApiPayload;
import com.hokhanh.session.response.common.CacheApiStatusType;

public record DeleteSharedFileApiPayload(
	BaseApiPayload metadata,
	CacheApiStatusType status,
	DeleteSharedFilePayload payload
) {

}
