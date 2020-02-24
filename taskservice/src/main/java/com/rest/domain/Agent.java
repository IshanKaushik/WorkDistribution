package com.rest.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Table(name = "AGENT")
@Entity
public class Agent implements Serializable {

	private static final long serialVersionUID = -9176831888542149742L;

	@Id
	@GeneratedValue
	@Column(name = "ID", updatable = false)
	private Integer id;

	@Column(name = "AGENT_ID")
	private Integer agentId;

	@Column(name = "SKILL")
	private String skill;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getAgentId() {
		return agentId;
	}

	public void setAgentId(Integer agentId) {
		this.agentId = agentId;
	}

	public String getSkill() {
		return skill;
	}

	public void setSkill(String skill) {
		this.skill = skill;
	}

}
