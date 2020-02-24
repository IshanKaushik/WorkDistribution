package com.rest.service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.PersistenceContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rest.domain.Agent;
import com.rest.domain.Task;
import com.rest.domain.repo.AgentRepository;
import com.rest.domain.repo.TaskRepository;
import com.rest.request.TaskRequest;
import com.rest.response.TaskResponse;

@Service
public class TaskAgentAPIService {

	@PersistenceContext
	private EntityManager entityManager;

	@Autowired
	private TaskRepository taskRepo;

	@Autowired
	private AgentRepository agentRepo;

	// or use this query -->
	// SELECT agent_id FROM AGENT where skill IN ('skill1', 'skill2', 'skill3')
	// GROUP BY agent_id HAVING COUNT(DISTINCT skill) = 3

	public List<Integer> getSkilledAgents(List<String> skills) {
		int size = skills.size();
		Map<Integer, Integer> myMap = new HashMap<Integer, Integer>();
		for (int i = 0; i < size; i++) {
			List<Agent> entityList = agentRepo.findBySkill(skills.get(i));

			for (int j = 0; j < entityList.size(); j++) {
				int count = myMap.containsKey(entityList.get(j).getAgentId())
						? myMap.get(entityList.get(j).getAgentId())
						: 0;
				myMap.put(entityList.get(j).getAgentId(), count + 1);
			}
		}
		List<Integer> returnList = new LinkedList<Integer>();
		Iterator it = myMap.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry<Integer, Integer> pair = (Map.Entry) it.next();
			if (size == pair.getValue()) {
				returnList.add(pair.getKey());
			}
		}
		return returnList;
	}

	public List<Integer> getFreeAgents(List<Integer> agents) {
		List<Integer> returnList = new LinkedList<Integer>();
		for (int i = 0; i < agents.size(); i++) {
			List<Task> entityList = taskRepo.findByAgentIdAndStatus(agents.get(i), 0);
			if (entityList.size() == 0) {
				// Free agent found
				returnList.add(agents.get(i));
			}
		}
		return returnList;
	}

	public TaskResponse findAndUpdateTask(List<Integer> agents, TaskRequest request) {
		TaskResponse returnResponse = new TaskResponse();
		Task recentEntity = new Task();
		for (int i = 0; i < agents.size(); i++) {
			// Get all ongoing task for the agent
			List<Task> entityList = taskRepo.findByAgentIdAndStatus(agents.get(i), 0);
			if (entityList.size() > 0) {
				// Check priority

				if (entityList.get(0).getPriority() < (request.getPriority().equals("high") ? 1 : 0)) {
					// Check recent time
					if (recentEntity.getId() == null
							|| recentEntity.getStartTime().isBefore(entityList.get(0).getStartTime())) {
						recentEntity = entityList.get(0);
					}
				}
			}
		}

		// If recentEntity not null then add a new task for the request
		// and update last task to pending
		if (recentEntity.getId() != null) {
			// First update current task and move to pending status
			recentEntity = taskRepo.findById(recentEntity.getId()).get();
			recentEntity.setStatus(-1);
			taskRepo.save(recentEntity);
			// add new task to in progress
			returnResponse = addTaskAndAgent(recentEntity.getAgentId(), request);
		}
		return returnResponse;
	}

	public TaskResponse addTaskAndAgent(Integer agentId, TaskRequest request) {
		// Add task
		Task taskEntity = new Task();
		taskEntity.setPriority(request.getPriority().equals("high") ? 1 : 0);
		taskEntity.setAgentId(agentId);
		taskEntity.setStatus(0);
		taskEntity.setStartTime(LocalDateTime.now());
		taskRepo.save(taskEntity);
		return convertEntitytoResponse(taskEntity, new TaskResponse());
	}

	private TaskResponse convertEntitytoResponse(Task taskEntity, TaskResponse taskResponse) {
		taskResponse.setAgentId(taskEntity.getAgentId());
		taskResponse.setPriority(taskEntity.getPriority() == 1 ? "high" : "low");
		taskResponse.setStartTime(taskEntity.getStartTime());
		taskResponse.setId(taskEntity.getId());
		taskResponse.setFinishTime(taskEntity.getFinishTime());
		taskResponse.setStatus(
				taskEntity.getStatus() == 1 ? "Completed" : taskEntity.getStatus() == 0 ? "In-progress" : "Pending");
		return taskResponse;
	}

	public TaskResponse findAndCompleteTask(Integer id) {

		// No task in progress
		TaskResponse response = new TaskResponse();
		// Find the entity
		Task entity = taskRepo.getOne(id);
		try {
			// This can throw an exception and need be captured
			if (entity.getId() == null) {
				response.setError("No Task found");
				return response;
			}
			if (entity.getStatus() != 0) {
				response.setError("Task not in progress!");
				return response;
			}
		} catch (EntityNotFoundException ex) {
			response.setError("No Task found");
			return response;
		}
		entity.setStatus(1);
		entity.setFinishTime(LocalDateTime.now());
		taskRepo.save(entity);

		// Get new task for the agent and move it to in-progress
		findNextTask(entity.getAgentId());

		return convertEntitytoResponse(entity, response);
	}

	private void findNextTask(Integer agentId) {
		List<Task> taskList = taskRepo.findByAgentIdAndStatus(agentId, -1);
		Task updateTask = new Task();
		for (int i = 0; i < taskList.size(); i++) {
			if (i == 0) {
				updateTask = taskList.get(i);
			} else if (updateTask.getPriority() < taskList.get(i).getPriority()
					&& updateTask.getStartTime().isAfter(taskList.get(i).getStartTime())) {
				updateTask = taskList.get(i);
			}
		}

		if (updateTask.getId() != null) {
			updateTask = taskRepo.getOne(updateTask.getId());
			updateTask.setStatus(0);
			taskRepo.save(updateTask);
		}

	}

	public List<TaskResponse> getAllAgents() {
		List<TaskResponse> responseList = new LinkedList<TaskResponse>();
		List<Task> entityList = taskRepo.findByStatusNot(1);
		for (Task entity : entityList) {
			responseList.add(convertEntitytoResponse(entity, new TaskResponse()));
		}

		return responseList;
	}

}
