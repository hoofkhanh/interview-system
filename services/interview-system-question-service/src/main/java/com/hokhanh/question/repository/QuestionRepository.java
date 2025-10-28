package com.hokhanh.question.repository;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.hokhanh.question.model.Question;

public interface QuestionRepository extends JpaRepository<Question, UUID> {
	boolean existsByTitleAndCreatorId(String title, UUID creatorId);
	
	boolean existsByTitleAndCreatorIdAndIdNot(String title, UUID creatorId, UUID id);
	
	Page<Question> findByTitleContainingIgnoreCaseAndCreatorId(String title, UUID creatorId, Pageable page);
}
