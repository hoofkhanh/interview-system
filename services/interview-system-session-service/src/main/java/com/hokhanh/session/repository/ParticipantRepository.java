package com.hokhanh.session.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hokhanh.session.model.Participant;

public interface ParticipantRepository extends JpaRepository<Participant, UUID>{
	boolean existsByUserIdAndSessionId(UUID userId, UUID sessionId);
}
