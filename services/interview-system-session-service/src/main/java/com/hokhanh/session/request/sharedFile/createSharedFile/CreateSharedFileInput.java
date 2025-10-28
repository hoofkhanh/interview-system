package com.hokhanh.session.request.sharedFile.createSharedFile;

import com.hokhanh.session.request.sharedFile.common.BaseFileInput;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

public record CreateSharedFileInput(
	@NotNull
	@Valid
	BaseFileInput baseFile
) {

}
