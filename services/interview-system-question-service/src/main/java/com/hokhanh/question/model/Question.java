package com.hokhanh.question.model;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import com.hokhanh.question.request.updateQuestion.UpdateQuestionInput;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
@Table(
	uniqueConstraints = @UniqueConstraint(columnNames = {"title", "creator_id"})
)
public class Question {

	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	private UUID id;
	
	@Column(nullable = false, name = "creator_id")
	private UUID creatorId;
	
	@OneToMany(mappedBy = "question", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<TestCase> testcases;
	
	@Column(nullable = false)
	private String title;
	
	@Column(nullable = false, columnDefinition = "TEXT")
	private String description;
	
	@Column(nullable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
	@Builder.Default
	private LocalDateTime createdAt = LocalDateTime.now();
	
	public void updateFromInput(UpdateQuestionInput input) {
		this.setTitle(input.baseQuestion().title());
		this.setDescription(input.baseQuestion().description());
	}
}
