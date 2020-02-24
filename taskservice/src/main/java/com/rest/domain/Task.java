package com.rest.domain;

import java.io.Serializable;
import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Table(name = "TASK")
@Entity
public class Task implements Serializable {

	private static final long serialVersionUID = -4021082344767991026L;

	@Id
	@GeneratedValue
	@Column(name = "ID", updatable = false)
	private Integer id;

	@Column(name = "PRIORITY")
	private Integer priority;

	@Column(name = "AGENT_ID")
	private Integer agentId;

	@Column(name = "START_TIME")
	private LocalDateTime startTime;

	@Column(name = "FINISH_TIME")
	private LocalDateTime finishTime;

	@Column(name = "STATUS")
	private Integer status;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getPriority() {
		return priority;
	}

	public void setPriority(Integer priority) {
		this.priority = priority;
	}

	public Integer getAgentId() {
		return agentId;
	}

	public void setAgentId(Integer agentId) {
		this.agentId = agentId;
	}

	public LocalDateTime getStartTime() {
		return startTime;
	}

	public void setStartTime(LocalDateTime startTime) {
		this.startTime = startTime;
	}

	public LocalDateTime getFinishTime() {
		return finishTime;
	}

	public void setFinishTime(LocalDateTime finishTime) {
		this.finishTime = finishTime;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

}
