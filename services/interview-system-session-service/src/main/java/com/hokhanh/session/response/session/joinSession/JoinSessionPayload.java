package com.hokhanh.session.response.session.joinSession;

import java.util.List;

import com.hokhanh.session.response.session.common.BaseSessionPayload;

public record JoinSessionPayload(
	List<MemberPayload> members,
	BaseSessionPayload baseSession
) {

}
