DROP TABLE IF EXISTS agent;
DROP TABLE IF EXISTS task;

CREATE TABLE agent (
  id INT AUTO_INCREMENT  PRIMARY KEY,
  agent_id INT,
  skill varchar(50)
);
 
INSERT INTO agent (agent_id, skill) VALUES
  (1,'skill1'),
  (1,'skill2'),
  (1,'skill3'),
  (2,'skill1'),
  (3,'skill2'),
  (4,'skill3'),
  (5,'skill1'),
  (5,'skill2');
  
  
CREATE TABLE task (
  id INT AUTO_INCREMENT  PRIMARY KEY,
  priority INT,
  agent_id INT,
  start_time DATE,
  finish_time DATE,
  status INT
);
