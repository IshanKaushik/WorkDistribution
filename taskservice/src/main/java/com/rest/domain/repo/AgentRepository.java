package com.rest.domain.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.rest.domain.Agent;

public interface AgentRepository extends JpaRepository<Agent, Integer> {

	List<Agent> findBySkill(String skill);
}