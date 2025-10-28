package com.hokhanh.question.controller;

import java.util.UUID;

import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.ContextValue;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import com.hokhanh.common.httpHeader.HttpHeadersConstants;
import com.hokhanh.question.request.createQuestion.CreateQuestionInput;
import com.hokhanh.question.request.deleteQuestion.DeleteQuestionInput;
import com.hokhanh.question.request.question.QuestionInput;
import com.hokhanh.question.request.questions.QuestionsInput;
import com.hokhanh.question.request.updateQuestion.UpdateQuestionInput;
import com.hokhanh.question.response.createQuestion.CreateQuestionApiPayload;
import com.hokhanh.question.response.deleteQuestion.DeleteQuestionApiPayload;
import com.hokhanh.question.response.question.QuestionApiPayload;
import com.hokhanh.question.response.questions.QuestionsApiPayload;
import com.hokhanh.question.response.updateQuestion.UpdateQuestionApiPayload;
import com.hokhanh.question.service.QuestionService;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class QuestionController {
	private final QuestionService service;

	@MutationMapping
	public CreateQuestionApiPayload createQuestion(@Argument @Valid CreateQuestionInput input,
			@ContextValue(name = HttpHeadersConstants.HEADER_USER_ID) UUID userId) {
		return service.createQuestion(input, userId);
	}
	
	@MutationMapping
	public UpdateQuestionApiPayload updateQuestion(@Argument @Valid UpdateQuestionInput input,
			@ContextValue(name = HttpHeadersConstants.HEADER_USER_ID) UUID userId) {
		return service.updateQuestion(input, userId);
	}
	
	@MutationMapping
	public DeleteQuestionApiPayload deleteQuestion(@Argument @Valid DeleteQuestionInput input,
			@ContextValue(name = HttpHeadersConstants.HEADER_USER_ID) UUID userId) {
		return service.deleteQuestion(input, userId);
	}
	
	@QueryMapping
	public QuestionsApiPayload questions(@Argument @Valid QuestionsInput input,
			@ContextValue(name = HttpHeadersConstants.HEADER_USER_ID) UUID userId) {
		return service.questions(input, userId);
	}
	
	@QueryMapping
	public QuestionApiPayload question(@Argument @Valid QuestionInput input,
			@ContextValue(name = HttpHeadersConstants.HEADER_USER_ID) UUID userId) {
		return service.question(input, userId);
	}
	
	@QueryMapping
	public UUID creatorIdByIdInternal(@Argument @NotBlank String id) {
		return service.creatorIdByIdInternal(id);
	}
}
