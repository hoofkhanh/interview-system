package com.hokhanh.question.service;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.hokhanh.question.mapper.QuestionMapper;
import com.hokhanh.question.model.Question;
import com.hokhanh.question.repository.QuestionRepository;
import com.hokhanh.question.request.createQuestion.CreateQuestionInput;
import com.hokhanh.question.request.deleteQuestion.DeleteQuestionInput;
import com.hokhanh.question.request.question.QuestionInput;
import com.hokhanh.question.request.questions.QuestionsInput;
import com.hokhanh.question.request.updateQuestion.UpdateQuestionInput;
import com.hokhanh.question.response.common.BaseApiPayload;
import com.hokhanh.question.response.common.QuestionApiStatusType;
import com.hokhanh.question.response.createQuestion.CreateQuestionApiPayload;
import com.hokhanh.question.response.deleteQuestion.DeleteQuestionApiPayload;
import com.hokhanh.question.response.question.QuestionApiPayload;
import com.hokhanh.question.response.questions.QuestionsApiPayload;
import com.hokhanh.question.response.updateQuestion.UpdateQuestionApiPayload;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class QuestionService {
	private final QuestionRepository questionRepo;
	private final QuestionMapper questionMapper;
	
	public CreateQuestionApiPayload createQuestion(CreateQuestionInput input, UUID userId) {
		boolean existingResult = questionRepo.existsByTitleAndCreatorId(input.baseQuestion().title(), userId);
		if(existingResult) {
			return new CreateQuestionApiPayload(
				new BaseApiPayload(false, "Title duplicated"),
				QuestionApiStatusType.TITLE_DUPLICATED,
				null
			);
		}
		
		Question question = questionRepo.save(questionMapper.toQuestion(input, userId));
		
		return new CreateQuestionApiPayload(
			new BaseApiPayload(true, "Created question successfully"),
			null,
			questionMapper.toCreateQuestionPayload(question)
		);
	}

	public UpdateQuestionApiPayload updateQuestion(UpdateQuestionInput input, UUID userId) {
		Question question = questionRepo.findById(UUID.fromString(input.id())).orElse(null);
		if(question == null) {
			return new UpdateQuestionApiPayload(
				new BaseApiPayload(false, "Question not found"),
				QuestionApiStatusType.QUESTION_NOT_FOUND,
				null
			);
		}else if(!question.getCreatorId().equals(userId)) {
			return new UpdateQuestionApiPayload(
				new BaseApiPayload(false, "You not owner"),
				QuestionApiStatusType.YOU_NOT_OWNER,
				null
			);
		}
		
		boolean checkedResult = questionRepo.existsByTitleAndCreatorIdAndIdNot(input.baseQuestion().title(), userId, UUID.fromString(input.id()));
		if(checkedResult) {
			return new UpdateQuestionApiPayload(
				new BaseApiPayload(false, "Title duplicated"),
				QuestionApiStatusType.TITLE_DUPLICATED,
				null
			);
		}
		
		// call existsByQuestionIdAndStatusInternal(questionId, ENDED) function of session service
		boolean existingResult = false;
		if(existingResult) {
			return new UpdateQuestionApiPayload(
				new BaseApiPayload(false, "Session ended"),
				QuestionApiStatusType.SESSION_ENDED,
				null
			);
		}
		
		question.updateFromInput(input);
		question = questionRepo.save(question);
		
		return new UpdateQuestionApiPayload(
			new BaseApiPayload(true, "Updated question successfully"),
			null,
			questionMapper.toUpdateQuestionPayload(question)
		);
	}

	public DeleteQuestionApiPayload deleteQuestion(DeleteQuestionInput input, UUID userId) {
		// call existsByQuestionIdInternal(questionId) function of session service
		boolean status = false;
		if(status) {
			return new DeleteQuestionApiPayload(
				new BaseApiPayload(false, "Attached in session"),
				QuestionApiStatusType.ATTACHED_IN_SESSION,
				null
			);
		}
		
		Question question = questionRepo.findById(UUID.fromString(input.id())).orElse(null);
		if(question == null) {
			return new DeleteQuestionApiPayload(
				new BaseApiPayload(false, "Question not found"),
				QuestionApiStatusType.QUESTION_NOT_FOUND,
				null
			);
		}else if(!question.getCreatorId().equals(userId)) {
			return new DeleteQuestionApiPayload(
				new BaseApiPayload(false, "You not owner"),
				QuestionApiStatusType.YOU_NOT_OWNER,
				null
			);
		}
		
		questionRepo.deleteById(UUID.fromString(input.id()));
		
		return new DeleteQuestionApiPayload(
			new BaseApiPayload(true, "Deleted successfully"),
			null,
			questionMapper.toDeleteQuestionPayload(question)
		);
	}

	public QuestionsApiPayload questions(QuestionsInput input, UUID userId) {
		Page<Question> questions = questionRepo.findByTitleContainingIgnoreCaseAndCreatorId
				(input.keyword(), userId, PageRequest.of(input.basePage().page(), input.basePage().size()));
		
		return new QuestionsApiPayload(
			new BaseApiPayload(true, "Questions successfully"),
			null,
			questionMapper.toQuestionsPayload(questions)
		);
	}

	public QuestionApiPayload question(QuestionInput input, UUID userId) {
		Question question = questionRepo.findById(UUID.fromString(input.id())).orElse(null);
		if(question == null) {
			return new QuestionApiPayload(
				new BaseApiPayload(false, "Question not found"),
				QuestionApiStatusType.QUESTION_NOT_FOUND,
				null
			);
		}
		// enable all people having question id is to can access this question
//		else if(!question.getCreatorId().equals(userId)) {
//			return new QuestionApiPayload(
//				new BaseApiPayload(false, "You not owner"),
//				QuestionApiStatusType.YOU_NOT_OWNER,
//				null
//			);
//		}
		
		
		return new QuestionApiPayload(
			new BaseApiPayload(true, "Question successfully"),
			null,
			questionMapper.toQuestionPayload(question)
		);
	}

	public UUID creatorIdByIdInternal(String id) {
		Question question = questionRepo.findById(UUID.fromString(id)).orElse(null);
		return question != null ? question.getCreatorId() : null;
	}

}
