package com.hokhanh.session.response.session.sessions;

public record BasePagePayload(
	Integer size,
	Integer page,
	Long totalElements,
	Integer totalPages
) {

}