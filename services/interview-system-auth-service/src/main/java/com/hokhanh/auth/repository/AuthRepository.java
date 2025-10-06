package com.hokhanh.auth.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.hokhanh.auth.model.Auth;

import io.lettuce.core.dynamic.annotation.Param;

public interface AuthRepository extends JpaRepository<Auth, UUID>{
	@Query("SELECT a.role.name FROM Auth a WHERE a.userId = :userId")
    Optional<String> findRoleNameByUserId(@Param("userId") UUID userId);
	
	Auth findByUserId(UUID userId);

	boolean existsByUsername(String username);
	
	Auth findByUsername(String username);
}
