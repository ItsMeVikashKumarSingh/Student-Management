# Student Management System

![Project Banner](https://via.placeholder.com/1200x300?text=Student+Management+System)

[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT)
[![Java](https://img.shields.io/badge/Java-17-orange)](https://www.java.com/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.0-green)](https://spring.io/projects/spring-boot)
[![MySQL](https://img.shields.io/badge/MySQL-8.0-blue)](https://www.mysql.com/)

A full-stack web application for managing students, teachers, and courses in an educational setting. It features role-based access control (Admin vs. User), CRUD operations, and connected entities (e.g., assigning courses to students/teachers via IDs with autocomplete suggestions). Built with Spring Boot for the backend and HTML/CSS/JS (with Bootstrap, jQuery, and AG Grid) for the frontend.

This project demonstrates advanced Spring Boot concepts like JPA with stored procedures, Spring Security, and RESTful APIs, integrated with a responsive UI.

## Table of Contents

* [Features](#features)
* [Tech Stack](#tech-stack)
* [Project Structure](#project-structure)
* [Installation](#installation)
* [Database Setup](#database-setup)
* [Usage](#usage)
* [Screenshots](#screenshots)
* [Contributing](#contributing)
* [License](#license)
* [Contact](#contact)

## Features

* **Role-Based Access**: Admins can add/update/delete records; Users can only view.
* **Entity Management**: CRUD for Students, Teachers, and Courses.
* **Relationships**: Students and Teachers are assigned to Courses via IDs, with autocomplete suggestions in forms.
* **Security**: In-memory authentication with Spring Security (extendable to JWT or DB-backed).
* **UI**: Responsive dashboard, data grids (AG Grid), forms with validation, and logout functionality.
* **Database**: Uses MySQL with stored procedures for operations.
* **Validation**: Backend checks for valid course IDs; Frontend has input hints and error alerts.

## Tech Stack

* **Backend**: Java 17, Spring Boot 3.x, Spring Security, JPA (Hibernate) with EntityManager for stored procedures.
* **Database**: MySQL 8.x (with schema and procedures provided).
* **Frontend**: HTML5, CSS (Bootstrap 5), JavaScript (jQuery, AG Grid for tables, jQuery UI for autocomplete).
* **Tools**: Maven for dependencies, Git for version control.

## Project Structure

```
StudentManagement/
├── src/
│   ├── main/
│   │   ├── java/com/example/StudentManagement/
│   │   │   ├── config/          # Security and app configs (e.g., SecurityConfig.java)
│   │   │   ├── controller/      # REST controllers (e.g., StudentController.java, TeacherController.java, CourseController.java, UserController.java)
│   │   │   ├── dao/            # Data Access Objects (e.g., StudentDao.java, TeacherDao.java, CourseDao.java)
│   │   │   ├── entity/         # JPA Entities (e.g., Student.java, Teacher.java, Course.java)
│   │   │   ├── service/        # Business services (e.g., StudentService.java, TeacherService.java, CourseService.java)
│   │   │   └── StudentManagementApplication.java  # Main app entry point
│   │   └── resources/
│   │       ├── static/
│   │       │   ├── css/        # Styles (e.g., shared.css)
│   │       │   ├── js/         # Scripts (e.g., shared.js)
│   │       │   ├── dashboard.html
│   │       │   ├── students.html
│   │       │   ├── teachers.html
│   │       │   └── courses.html
│   │       ├── application.properties  # Config (e.g., DB connection)
│   │       └── templates/      # If using Thymeleaf (optional)
│   └── test/                   # Unit/integration tests
├── pom.xml                     # Maven dependencies
├── README.md                   # This file
└── sql/                        # (Optional folder) Database scripts (e.g., schema.sql with tables and procedures)
```

* **Where to Add README**: Place this `README.md` file in the root directory of your project (e.g., `/StudentManagement/README.md`). This is standard for GitHub repositories.

## Installation

1. **Prerequisites**:

    * Java 17+ (JDK)
    * Maven 3.x
    * MySQL 8.x server
    * Git

2. **Clone the Repository**:

   ```bash
   ```

git clone [https://github.com/yourusername/student-management-system.git](https://github.com/yourusername/student-management-system.git)
cd student-management-system

````

3. **Build the Project**:
```bash
mvn clean install
````

4. **Configure application.properties** (in `src/main/resources/`):

   ```properties
   ```

spring.datasource.url=jdbc\:mysql://localhost:3306/studentdb?useSSL=false\&serverTimezone=UTC
spring.datasource.username=root
spring.datasource.password=yourpassword
spring.jpa.hibernate.ddl-auto=none  # Use 'update' for dev

````

5. **Run the Application**:
```bash
mvn spring-boot:run
````

* Access at `http://localhost:8080/dashboard.html` (after login).

## Database Setup

1. Create the database:

   ```sql
   ```

CREATE DATABASE studentdb;

````

2. Run the following SQL script to create tables and stored procedures (copy to a file like `sql/schema.sql` and execute in MySQL Workbench or CLI):

```sql
USE studentdb;

-- Create tables
CREATE TABLE coursetable (
id INT AUTO_INCREMENT PRIMARY KEY,
name VARCHAR(255) NOT NULL,
description TEXT
);

CREATE TABLE studenttable (
roll INT AUTO_INCREMENT PRIMARY KEY,
name VARCHAR(100) NOT NULL,
email VARCHAR(100) NOT NULL,
age INT NOT NULL,
course_id INT NOT NULL,
FOREIGN KEY (course_id) REFERENCES coursetable(id) ON DELETE RESTRICT
);

CREATE TABLE teachertable (
id INT AUTO_INCREMENT PRIMARY KEY,
name VARCHAR(255) NOT NULL,
email VARCHAR(255) NOT NULL,
course_id INT NOT NULL,
FOREIGN KEY (course_id) REFERENCES coursetable(id) ON DELETE RESTRICT
);

-- Stored Procedures
DELIMITER //

CREATE PROCEDURE sp_add_student(
IN p_name VARCHAR(100),
IN p_email VARCHAR(100),
IN p_age INT,
IN p_course_id INT,
OUT p_roll INT
)
BEGIN
INSERT INTO studenttable (name, email, age, course_id) VALUES (p_name, p_email, p_age, p_course_id);
SET p_roll = LAST_INSERT_ID();
END //

CREATE PROCEDURE sp_get_students()
BEGIN
SELECT s.roll, s.name, s.email, s.age, c.name AS course_name, s.course_id
FROM studenttable s
LEFT JOIN coursetable c ON s.course_id = c.id
ORDER BY s.roll;
END //

CREATE PROCEDURE sp_update_student(
IN p_roll INT,
IN p_name VARCHAR(100),
IN p_email VARCHAR(100),
IN p_age INT,
IN p_course_id INT
)
BEGIN
UPDATE studenttable
SET name = p_name, email = p_email, age = p_age, course_id = p_course_id
WHERE roll = p_roll;
END //

CREATE PROCEDURE sp_delete_student(IN p_roll INT)
BEGIN
DELETE FROM studenttable WHERE roll = p_roll;
END //

CREATE PROCEDURE sp_add_teacher(
IN p_name VARCHAR(255),
IN p_email VARCHAR(255),
IN p_course_id INT,
OUT p_id INT
)
BEGIN
INSERT INTO teachertable (name, email, course_id) VALUES (p_name, p_email, p_course_id);
SET p_id = LAST_INSERT_ID();
END //

CREATE PROCEDURE sp_get_teachers()
BEGIN
SELECT t.id, t.name, t.email, c.name AS course_name, t.course_id
FROM teachertable t
LEFT JOIN coursetable c ON t.course_id = c.id;
END //

CREATE PROCEDURE sp_update_teacher(
IN p_id INT,
IN p_name VARCHAR(255),
IN p_email VARCHAR(255),
IN p_course_id INT
)
BEGIN
UPDATE teachertable
SET name = p_name, email = p_email, course_id = p_course_id
WHERE id = p_id;
END //

CREATE PROCEDURE sp_delete_teacher(IN p_id INT)
BEGIN
DELETE FROM teachertable WHERE id = p_id;
END //

CREATE PROCEDURE sp_add_course(
IN p_name VARCHAR(255),
IN p_description TEXT,
OUT p_id INT
)
BEGIN
INSERT INTO coursetable (name, description) VALUES (p_name, p_description);
SET p_id = LAST_INSERT_ID();
END //

CREATE PROCEDURE sp_get_courses()
BEGIN
SELECT id, name, description FROM coursetable;
END //

CREATE PROCEDURE sp_update_course(
IN p_id INT,
IN p_name VARCHAR(255),
IN p_description TEXT
)
BEGIN
UPDATE coursetable SET name = p_name, description = p_description WHERE id = p_id;
END //

CREATE PROCEDURE sp_delete_course(IN p_id INT)
BEGIN
DELETE FROM coursetable WHERE id = p_id;
END //

DELIMITER ;
````

3. (Optional) Seed data:

```sql
INSERT INTO coursetable (name, description) VALUES ('Math', 'Basic Math');
```

## Usage

* **Login**: Use in-memory credentials (admin/password for Admin, user/password for User) – extend for production.
* **Dashboard**: Overview with links to manage entities.
* **Students/Teachers**: Admins add/edit/delete with course autocomplete; Users view grids.
* **Courses**: Independent CRUD.
* **API Endpoints**: e.g., POST `/students/add` with JSON `{ "name": "John", "email": "john@example.com", "age": 20, "courseId": 1 }`.

For full details, see the code and run locally.

## Screenshots

![Dashboard](https://via.placeholder.com/800x400?text=Dashboard+Screenshot)
![Student Management](https://via.placeholder.com/800x400?text=Students+Page+Screenshot)

(Add screenshots to `/docs/screenshots/` and link them here.)

## Contributing

1. Fork the repository.
2. Create a feature branch (`git checkout -b feature/AmazingFeature`).
3. Commit changes (`git commit -m 'Add some AmazingFeature'`).
4. Push to the branch (`git push origin feature/AmazingFeature`).
5. Open a Pull Request.

Please follow code style (e.g., Java conventions) and add tests.

## License

Distributed under the MIT License. See `LICENSE` for more information. (Add a LICENSE file to root if not present.)

## Contact

Your Name - [@yourtwitter](https://twitter.com/yourtwitter) - [email@example.com](mailto:email@example.com)

Project Link: [https://github.com/yourusername/student-management-system](https://github.com/yourusername/student-management-system)
