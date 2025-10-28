package com.hokhanh.question.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hokhanh.question.model.TestCase;

public interface TestCaseRepository extends JpaRepository<TestCase, UUID> {

}
