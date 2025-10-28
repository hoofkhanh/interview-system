package com.hokhanh.session.controller;

import java.util.UUID;

import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.ContextValue;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.stereotype.Controller;

import com.hokhanh.common.httpHeader.HttpHeadersConstants;
import com.hokhanh.session.request.sharedFile.createSharedFile.CreateSharedFileInput;
import com.hokhanh.session.request.sharedFile.deleteSharedFile.DeleteSharedFileInput;
import com.hokhanh.session.request.sharedFile.updateSharedFile.UpdateSharedFileInput;
import com.hokhanh.session.response.sharedFile.createSharedFile.CreateSharedFileApiPayload;
import com.hokhanh.session.response.sharedFile.deleteSharedFile.DeleteSharedFileApiPayload;
import com.hokhanh.session.response.sharedFile.updateSharedFile.UpdateSharedFileApiPayload;
import com.hokhanh.session.service.FileService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class FileController {

	private final FileService service;
	
	@MutationMapping
	public CreateSharedFileApiPayload createSharedFile(@Argument @Valid CreateSharedFileInput input,
			@ContextValue(name = HttpHeadersConstants.HEADER_USER_ID) UUID userId) {
		return service.createSharedFile(input, userId);
	}
	
	@MutationMapping
	public UpdateSharedFileApiPayload updateSharedFile(@Argument @Valid UpdateSharedFileInput input,
			@ContextValue(name = HttpHeadersConstants.HEADER_USER_ID) UUID userId) {
		return service.updateSharedFile(input, userId);
	}
	
	@MutationMapping
	public DeleteSharedFileApiPayload deleteSharedFile(@Argument @Valid DeleteSharedFileInput input,
			@ContextValue(name = HttpHeadersConstants.HEADER_USER_ID) UUID userId) {
		return service.deleteSharedFile(input, userId);
	}
	
	
}
