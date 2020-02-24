package com.rest.domain.repo;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.rest.domain.Task;
import com.rest.response.TaskResponse;

public interface TaskRepository extends JpaRepository<Task, Integer> {

	List<Task> findByAgentIdAndStatus(Integer agentId, int status);

	List<Task> findByStatusNot(int i);
}