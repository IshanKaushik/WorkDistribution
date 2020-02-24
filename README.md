# WorkDistribution
Description : Work distribution for provided agents with given skills


This project is built with Spring boot 2 framework. Exposed are 3 API endpoints :
1. http://localhost:8098/task-service/api/task/add

    This enpoint adds the new task with given priority ("low"/"high") and skills ("skill1", "skill2" and "skill3)
    Response will contain a task onject with task id this id can later be used for endpoint 2 to update task

2. http://localhost:8098/task-service/api/task/update

   This endpoint will update task to complete if the task is in progress
   
3. http://localhost:8098/task-service/api/task/view

   This endpoint will display all ongoing and pending task in queue

To start the project :


Unit testing via postman :
