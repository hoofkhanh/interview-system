package com.hokhanh.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hokhanh.model.User;

public interface UserRepository extends JpaRepository<User, UUID>{

	User findByEmail(String email);

}
