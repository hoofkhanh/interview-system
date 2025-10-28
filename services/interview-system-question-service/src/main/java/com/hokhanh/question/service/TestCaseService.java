package com.hokhanh.question.service;

import java.util.UUID;

import org.springframework.stereotype.Service;

import com.hokhanh.question.mapper.TestCaseMapper;
import com.hokhanh.question.model.Question;
import com.hokhanh.question.model.TestCase;
import com.hokhanh.question.repository.QuestionRepository;
import com.hokhanh.question.repository.TestCaseRepository;
import com.hokhanh.question.request.createTestCase.CreateTestCaseInput;
import com.hokhanh.question.request.deleteTestCase.DeleteTestCaseInput;
import com.hokhanh.question.request.updateTestCase.UpdateTestCaseInput;
import com.hokhanh.question.response.common.BaseApiPayload;
import com.hokhanh.question.response.common.TestCaseApiStatusType;
import com.hokhanh.question.response.createTestCase.CreateTestCaseApiPayload;
import com.hokhanh.question.response.deleteTestCase.DeleteTestCaseApiPayload;
import com.hokhanh.question.response.updateTestCase.UpdateTestCaseApiPayload;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TestCaseService {
	private final TestCaseRepository testCaseRepo;
	private final QuestionRepository questionRepo;
	
	private final TestCaseMapper testCaseMapper;

	public CreateTestCaseApiPayload createTestCase(CreateTestCaseInput input, UUID userId) {
		Question question = questionRepo.findById(UUID.fromString(input.questionId())).orElse(null);
		if(question == null) {
			return new CreateTestCaseApiPayload(
				new BaseApiPayload(false, "Question not found"),
				TestCaseApiStatusType.QUESTION_NOT_FOUND,
				null
			);
		}else if (!question.getCreatorId().equals(userId)) {
			return new CreateTestCaseApiPayload(
				new BaseApiPayload(false, "You not owner"),
				TestCaseApiStatusType.YOU_NOT_OWNER,
				null
			);
		}
		
		TestCase testCase = testCaseRepo.save(testCaseMapper.toTestCase(input, question));
		return new CreateTestCaseApiPayload(
			new BaseApiPayload(true, "Created test case successfully"),
			null,
			testCaseMapper.toCreateTestCasePayload(testCase)
		);
	}

	public UpdateTestCaseApiPayload updateTestCase(UpdateTestCaseInput input, UUID userId) {
		TestCase testCase = testCaseRepo.findById(UUID.fromString(input.id())).orElse(null);
		if(testCase == null) {
			return new UpdateTestCaseApiPayload(
				new BaseApiPayload(false, "Test case not found"),
				TestCaseApiStatusType.TEST_CASE_NOT_FOUND,
				null
			);
		}else if(!testCase.getQuestion().getCreatorId().equals(userId)) {
			return new UpdateTestCaseApiPayload(
				new BaseApiPayload(false, "You not owner"),
				TestCaseApiStatusType.YOU_NOT_OWNER,
				null
			);
		}
		
		testCase.updateFromInput(input);
		testCase = testCaseRepo.save(testCase);
		
		return new UpdateTestCaseApiPayload(
			new BaseApiPayload(true, "Updated successfully"),
			null,
			testCaseMapper.toUpdateTestCasePayload(testCase)
		);
	}

	public DeleteTestCaseApiPayload deleteTestCase(DeleteTestCaseInput input, UUID userId) {
		TestCase testCase = testCaseRepo.findById(UUID.fromString(input.id())).orElse(null);
		if(testCase == null) {
			return new DeleteTestCaseApiPayload(
				new BaseApiPayload(false, "Test case not found"),
				TestCaseApiStatusType.TEST_CASE_NOT_FOUND,
				null
			);
		}else if(!testCase.getQuestion().getCreatorId().equals(userId)) {
			return new DeleteTestCaseApiPayload(
				new BaseApiPayload(false, "You not owner"),
				TestCaseApiStatusType.YOU_NOT_OWNER,
				null
			);
		}
		
		testCaseRepo.deleteById(UUID.fromString(input.id()));
		
		return new DeleteTestCaseApiPayload(
			new BaseApiPayload(true, "Deleted successfully"),
			null,
			testCaseMapper.toDeleteTestCasePayload(testCase)
		);
	}
}
