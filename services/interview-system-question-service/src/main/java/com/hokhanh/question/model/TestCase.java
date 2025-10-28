package com.hokhanh.question.model;

import java.util.UUID;

import com.hokhanh.question.request.updateTestCase.UpdateTestCaseInput;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
public class TestCase {

	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	private UUID id;
	
	@ManyToOne(optional = false)
	@JoinColumn(name = "question_id", nullable = false)
	private Question question;
	
	@Column(nullable = false, columnDefinition = "TEXT")
	private String input;

	@Column(nullable = false, columnDefinition = "TEXT")
	private String output;
	
	@Column(nullable = false, columnDefinition = "BOOLEAN DEFAULT FALSE")
	@Builder.Default
	private Boolean isHidden = false;
	
	public void updateFromInput(UpdateTestCaseInput input) {
		this.setInput(input.baseTestCase().input());
		this.setOutput(input.baseTestCase().output());
		this.setIsHidden(input.baseTestCase().isHidden());
	}
}
