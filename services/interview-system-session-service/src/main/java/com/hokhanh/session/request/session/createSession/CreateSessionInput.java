package com.hokhanh.session.request.session.createSession;


import com.hokhanh.session.request.session.common.BaseSessionInput;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

public record CreateSessionInput(
	@Valid
	@NotNull(message = "baseSession is required")
	BaseSessionInput baseSession
) {

}
