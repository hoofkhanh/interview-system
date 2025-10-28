package com.hokhanh.session.repository;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.hokhanh.session.model.Session;

public interface SessionRepository extends JpaRepository<Session, UUID> {
	boolean existsByTitleAndCreatorId(String title, UUID creatorId);
	
	boolean existsByTitleAndCreatorIdAndIdNot(String title, UUID creatorId, UUID id);
	
	Page<Session> findByCreatorId(UUID creatorId, Pageable page);
}
