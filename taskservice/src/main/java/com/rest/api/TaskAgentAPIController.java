package com.rest.api;

import java.util.LinkedList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.rest.request.TaskRequest;
import com.rest.request.UpdateTaskRequest;
import com.rest.response.TaskResponse;
import com.rest.service.TaskAgentAPIService;

@RestController
@RequestMapping("/api/task")
public class TaskAgentAPIController {

	@Autowired
	private TaskAgentAPIService taskAgentAPIService;

	@RequestMapping(value = "/add", method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity addTask(@RequestBody TaskRequest request) {
		// Response object
		TaskResponse response = new TaskResponse();

		if (request == null || request.getPriority() == null || request.getSkills() == null) {
			response.setError("Bad request");
			return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
		}
		// Task taskEntity = taskAgentAPIService.addTask(request);

		// Get skilled agent
		List<Integer> agents = taskAgentAPIService.getSkilledAgents(request.getSkills());

		// Check if Agents are free
		List<Integer> freeAgents = taskAgentAPIService.getFreeAgents(agents);
		if (freeAgents.size() > 0) {
			// Add and assign task to the first agent
			response = taskAgentAPIService.addTaskAndAgent(freeAgents.get(0), request);
		} else {
			// Free Agent not found, check priority, add and assign task
			response = taskAgentAPIService.findAndUpdateTask(agents, request);
			if (response.getId() == null) {
				// No ongoing task found with lesser priority
				response.setError("No Agent found");
				return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
			}
		}

		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@RequestMapping(value = "/update", method = RequestMethod.PUT)
	@ResponseBody
	public ResponseEntity updateTask(@RequestBody UpdateTaskRequest request) {
		// Response object
		TaskResponse response = new TaskResponse();
		if (request == null || request.getId() == null) {
			response.setError("Bad request");
			return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
		}
		
		response = taskAgentAPIService.findAndCompleteTask(request.getId());
		if(response.getId() == null) {
			return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
		}
		return new ResponseEntity<>(response, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/view", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity ViewTask() {
		List<TaskResponse> responseList = new LinkedList<TaskResponse>();
		responseList = taskAgentAPIService.getAllAgents();
		
		return new ResponseEntity<>(responseList, HttpStatus.OK);
	}

}
