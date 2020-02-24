# WorkDistribution
Description : Work distribution for provided agents with given skills


This project is built with Spring boot 2 framework. It utilies fat jar depoyement with nested Tomcat server and H2 database

###### Exposed are 3 API endpoints:
1. http://localhost:8098/task-service/api/task/add

    This enpoint adds the new task with given priority ("low"/"high") and skills ("skill1", "skill2" and "skill3)
    Response will contain a task onject with task id this id can later be used for endpoint 2 to update task

2. http://localhost:8098/task-service/api/task/update

   This endpoint will update task to complete if the task is in progress
   
3. http://localhost:8098/task-service/api/task/view

   This endpoint will display all ongoing and pending task in queue
   
###### Start project with pre-compiled JAR file:
Due to reduced size limit I have uploaded JAR file at following location : 
https://drive.google.com/file/d/1uvkAKNw3kBIWOYr3bwCZgoO3uFXROaEA/view?usp=sharing

Step to follow :

    1. Download jar file on your windows
    2. Open command promt in admin mode and navigate to the downloaded jar file folder
    3. Execute command : java -jar taskservice.jar (assuming java is available on your system)

###### Compile and start the project :
    Import project in eclipse and run the application as Java project.


###### Unit testing via postman :
    Collection is provided for all 3 endpoints. Import it in postman tool and execute.
