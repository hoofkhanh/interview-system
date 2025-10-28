package com.hokhanh.question.response.questions;

public record BasePagePayload(
	Integer size,
	Integer page,
	Long totalElements,
	Integer totalPages
) {

}