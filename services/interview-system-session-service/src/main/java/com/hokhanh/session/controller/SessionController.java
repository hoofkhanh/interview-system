package com.hokhanh.session.controller;

import java.util.UUID;

import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.ContextValue;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import com.hokhanh.common.httpHeader.HttpHeadersConstants;
import com.hokhanh.session.request.session.createSession.CreateSessionInput;
import com.hokhanh.session.request.session.deleteSession.DeleteSessionInput;
import com.hokhanh.session.request.session.joinSession.JoinSessionInput;
import com.hokhanh.session.request.session.updateSession.UpdateSessionInput;
import com.hokhanh.session.response.session.createSession.CreateSessionApiPayload;
import com.hokhanh.session.response.session.deleteSession.DeleteSessionApiPayload;
import com.hokhanh.session.response.session.joinSession.JoinSessionApiPayload;
import com.hokhanh.session.response.session.sessions.SessionsPayload;
import com.hokhanh.session.response.session.updateSession.UpdateSessionApiPayload;
import com.hokhanh.session.service.SessionService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class SessionController {
	private final SessionService service;
	
	@MutationMapping
	public CreateSessionApiPayload createSession(@Argument @Valid CreateSessionInput input,
			@ContextValue(name = HttpHeadersConstants.HEADER_USER_ID) UUID userId) {
		return service.createSession(input, userId);
	}
	
	@MutationMapping
	public UpdateSessionApiPayload updateSession(@Argument @Valid UpdateSessionInput input,
			@ContextValue(name = HttpHeadersConstants.HEADER_USER_ID) UUID userId) {
		return service.updateSession(input, userId);
	}
	
	@MutationMapping
	public DeleteSessionApiPayload deleteSession(@Argument @Valid DeleteSessionInput input,
			@ContextValue(name = HttpHeadersConstants.HEADER_USER_ID) UUID userId) {
		return service.deleteSession(input, userId);
	}

	@MutationMapping
	public JoinSessionApiPayload joinSession(@Argument @Valid JoinSessionInput input,
			@ContextValue(name = HttpHeadersConstants.HEADER_USER_ID) UUID userId) {
		return service.joinSession(input, userId);
	}
	
	@QueryMapping
	public SessionsPayload sessions(@Argument int page, @Argument int size,
			@ContextValue(name = HttpHeadersConstants.HEADER_USER_ID) UUID userId) {
		return service.sessions(page, size, userId);
	}
	
	@MutationMapping
	public UUID endSession(@Argument String sessionId,
			@ContextValue(name = HttpHeadersConstants.HEADER_USER_ID) UUID userId) {
		return service.endSession(sessionId, userId);
	}
}
