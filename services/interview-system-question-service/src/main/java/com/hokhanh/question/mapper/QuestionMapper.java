package com.hokhanh.question.mapper;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import com.hokhanh.question.model.Question;
import com.hokhanh.question.request.createQuestion.CreateQuestionInput;
import com.hokhanh.question.response.common.BaseQuestionPayload;
import com.hokhanh.question.response.common.BaseTestCasePayload;
import com.hokhanh.question.response.createQuestion.CreateQuestionPayload;
import com.hokhanh.question.response.deleteQuestion.DeleteQuestionPayload;
import com.hokhanh.question.response.question.QuestionPayload;
import com.hokhanh.question.response.questions.BasePagePayload;
import com.hokhanh.question.response.questions.QuestionsPayload;
import com.hokhanh.question.response.updateQuestion.UpdateQuestionPayload;

@Service
public class QuestionMapper {

	public Question toQuestion(CreateQuestionInput input, UUID userId) {
		return Question.builder()
				.creatorId(userId)
				.title(input.baseQuestion().title())
				.description(input.baseQuestion().description())
				.createdAt(LocalDateTime.now())
				.build();
	}
	
	private BaseQuestionPayload toBaseQuestionPayload(Question question) {
		return new BaseQuestionPayload(
			question.getId(),
			question.getCreatorId(),
			question.getTitle(),
			question.getDescription(),
			question.getCreatedAt()
		);
	}

	public CreateQuestionPayload toCreateQuestionPayload(Question question) {
		return new CreateQuestionPayload(
			toBaseQuestionPayload(question)
		);
	}

	public UpdateQuestionPayload toUpdateQuestionPayload(Question question) {
		return new UpdateQuestionPayload(
			toBaseQuestionPayload(question)
		);
	}

	public DeleteQuestionPayload toDeleteQuestionPayload(Question question) {
		return new DeleteQuestionPayload(question.getId());
	}

	public QuestionsPayload toQuestionsPayload(Page<Question> questions) {
		BasePagePayload basePage = new BasePagePayload(
				questions.getSize(), 
				questions.getNumber(), 
				questions.getTotalElements(), 
				questions.getTotalPages()
		);
		
		List<BaseQuestionPayload> baseQuestions = questions.map(q -> toBaseQuestionPayload(q)).getContent();
		
		return new QuestionsPayload(basePage, baseQuestions);
	}

	public QuestionPayload toQuestionPayload(Question question) {
		BaseQuestionPayload baseQuestion = new BaseQuestionPayload(
			question.getId(),
			question.getCreatorId(),
			question.getTitle(),
			question.getDescription(),
			question.getCreatedAt()
		);
		
		List<BaseTestCasePayload> testCases = question.getTestcases().stream()
			.map(t -> new BaseTestCasePayload(
				t.getId(),
				t.getInput(),
				t.getOutput(),
				t.getIsHidden()
			)
		).toList();
		
		return new QuestionPayload(baseQuestion, testCases);
	}
}
