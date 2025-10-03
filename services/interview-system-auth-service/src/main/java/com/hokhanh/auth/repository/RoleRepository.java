package com.hokhanh.auth.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hokhanh.auth.model.Role;

public interface RoleRepository extends JpaRepository<Role, Long>{

}
