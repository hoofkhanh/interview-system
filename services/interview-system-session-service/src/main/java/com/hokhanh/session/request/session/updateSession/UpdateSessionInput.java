package com.hokhanh.session.request.session.updateSession;


import com.hokhanh.session.request.session.common.BaseSessionInput;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record UpdateSessionInput(
	@NotNull(message = "baseSession is required")
	@Valid
	BaseSessionInput baseSession,
	
	@NotBlank(message = "id is required")
	String id
) {

}
