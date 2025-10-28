package com.hokhanh.session.request.sharedFile.updateSharedFile;

import com.hokhanh.session.request.sharedFile.common.BaseFileInput;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record UpdateSharedFileInput(
	@NotNull
	@Valid
	BaseFileInput baseFile,
	
	@NotBlank
	String id
) {

}
