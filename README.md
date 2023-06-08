## Web-site for articles on different themes (created in 2018)
## Check out specification file for full description


## Technologies:
#### Backend: Java, Spring Boot, Websocket (SockJS, STOMP)
#### Frontend: Thymeleaf, javascript, jQuery, Bootstrap 4
#### CSS: Bootstrap 4
#### DB: MySQL, Postgresql

![Alt text](/description/main.PNG "Topics list")
![Alt text](/description/admin.PNG "Topics list")
![Alt text](/description/category-management.PNG "Add topic page")
![Alt text](/description/article.PNG "Main page")
![Alt text](/description/save-as-pdf.PNG "Topic page")
![Alt text](/description/sql-terminal.PNG "Topic page")

## Getting started (instruction has done in a hurry)
#### 1. Clone project from github
#### 2. Use jdk 8
#### 3. Create mysql database db1
#### 4. open application.properties and change spring.jpa.hibernate.ddl-auto to create
#### 5. launch app, data schema will be generated
#### 6. stop app, change spring.jpa.hibernate.ddl-auto to none
#### 7. add 1st user
#### 8. open mysql shell, use db1 and insert file "data-mysql.sql" contents (it will make this user admin)
#### 9. login again

