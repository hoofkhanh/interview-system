package com.hokhanh.question.mapper;

import org.springframework.stereotype.Service;

import com.hokhanh.question.model.Question;
import com.hokhanh.question.model.TestCase;
import com.hokhanh.question.request.createTestCase.CreateTestCaseInput;
import com.hokhanh.question.response.common.BaseTestCasePayload;
import com.hokhanh.question.response.createTestCase.CreateTestCasePayload;
import com.hokhanh.question.response.deleteTestCase.DeleteTestCasePayload;
import com.hokhanh.question.response.updateTestCase.UpdateTestCasePayload;

@Service
public class TestCaseMapper {

	public TestCase toTestCase(CreateTestCaseInput input, Question question) {
		return TestCase.builder()
				.question(question)
				.input(input.baseTestCase().input())
				.output(input.baseTestCase().output())
				.isHidden(input.baseTestCase().isHidden())
				.build();
	}
	
	private BaseTestCasePayload toBaseTestCasePayload(TestCase testCase) {
		return new BaseTestCasePayload(
			testCase.getId(),
			testCase.getInput(),
			testCase.getOutput(),
			testCase.getIsHidden()
		);
	}

	public CreateTestCasePayload toCreateTestCasePayload(TestCase testCase) {
		return new CreateTestCasePayload(toBaseTestCasePayload(testCase));
	}

	public UpdateTestCasePayload toUpdateTestCasePayload(TestCase testCase) {
		return new UpdateTestCasePayload(toBaseTestCasePayload(testCase));
	}

	public DeleteTestCasePayload toDeleteTestCasePayload(TestCase testCase) {
		return new DeleteTestCasePayload(testCase.getId());
	}
	

}
