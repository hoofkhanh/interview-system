package com.hokhanh.session.response.sharedFile.updateSharedFile;

import com.hokhanh.session.response.common.BaseApiPayload;
import com.hokhanh.session.response.common.CacheApiStatusType;

public record UpdateSharedFileApiPayload(
	BaseApiPayload metadata,
	CacheApiStatusType status,
	UpdateSharedFilePayload payload
) {

}
