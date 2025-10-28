package com.hokhanh.question.response.questions;

import java.util.List;

import com.hokhanh.question.response.common.BaseQuestionPayload;

public record QuestionsPayload(
	BasePagePayload basePage,
	List<BaseQuestionPayload> baseQuestions
) {

}
