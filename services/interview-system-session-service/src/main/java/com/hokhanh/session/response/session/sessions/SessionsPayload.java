package com.hokhanh.session.response.session.sessions;

import java.util.List;

import com.hokhanh.session.response.session.common.BaseSessionPayload;

public record SessionsPayload(List<BaseSessionPayload> baseSessions, BasePagePayload basePage ) {

}
