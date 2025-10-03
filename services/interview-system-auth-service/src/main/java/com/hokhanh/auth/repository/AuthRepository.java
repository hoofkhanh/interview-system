package com.hokhanh.auth.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hokhanh.auth.model.Auth;

public interface AuthRepository extends JpaRepository<Auth, UUID>{

}
