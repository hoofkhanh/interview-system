package com.hokhanh.user.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hokhanh.user.model.User;

public interface UserRepository extends JpaRepository<User, UUID>{

	User findByEmail(String email);

}
