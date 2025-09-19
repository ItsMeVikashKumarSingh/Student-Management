# Student Management System

<div align="center">
  <img src="docs/images/student_management_logo.png" alt="Student Management System" style="max-width: 100%; width: 350px; height: auto;">
</div>

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

git clone [https://github.com/ItsMeVikashKumarSingh/Student-Management.git](https://github.com/ItsMeVikashKumarSingh/Student-Management.git)
cd student-management

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
use studentdb;

DROP PROCEDURE IF EXISTS sp_manage;
DELIMITER $$
CREATE
    DEFINER = `root`@`localhost`
PROCEDURE sp_manage(IN p_entity VARCHAR(16), IN p_action VARCHAR(16), IN p_payload JSON)
BEGIN
  -- =========================
        ---- COURSE ----
  -- =========================
  IF p_entity = 'COURSE' THEN
    IF p_action = 'ADD' THEN
      INSERT INTO coursetable(name, description)
      VALUES (
        JSON_UNQUOTE(JSON_EXTRACT(p_payload, '$.name')),
        JSON_UNQUOTE(JSON_EXTRACT(p_payload, '$.description'))
      );
      SELECT LAST_INSERT_ID() AS new_id;
    ELSEIF p_action = 'UPDATE' THEN
      UPDATE coursetable
        SET name = COALESCE(JSON_UNQUOTE(JSON_EXTRACT(p_payload, '$.name')), name),
            description = COALESCE(JSON_UNQUOTE(JSON_EXTRACT(p_payload, '$.description')), description)
      WHERE id = CAST(JSON_UNQUOTE(JSON_EXTRACT(p_payload, '$.id')) AS UNSIGNED);
      SELECT CAST(JSON_UNQUOTE(JSON_EXTRACT(p_payload, '$.id')) AS UNSIGNED) AS updated_id;
    ELSEIF p_action = 'DELETE' THEN
      DELETE FROM coursetable
      WHERE id = CAST(JSON_UNQUOTE(JSON_EXTRACT(p_payload, '$.id')) AS UNSIGNED);
      SELECT CAST(JSON_UNQUOTE(JSON_EXTRACT(p_payload, '$.id')) AS UNSIGNED) AS deleted_id;
    ELSEIF p_action = 'GET' THEN
      SELECT id, name, description FROM coursetable;
    ELSEIF p_action = 'GET_COURSE_NAME' THEN
      SELECT name
      FROM coursetable
      WHERE id = CAST(JSON_UNQUOTE(JSON_EXTRACT(p_payload, '$.id')) AS UNSIGNED);
    END IF;

  -- =========================
        ---- STUDENT ----
  -- =========================
  ELSEIF p_entity = 'STUDENT' THEN
    IF p_action = 'ADD' THEN
      INSERT INTO studenttable(name, email, age, course_id, profile_picture_name)
      VALUES (
        JSON_UNQUOTE(JSON_EXTRACT(p_payload, '$.name')),
        JSON_UNQUOTE(JSON_EXTRACT(p_payload, '$.email')),
        CAST(JSON_UNQUOTE(JSON_EXTRACT(p_payload, '$.age')) AS SIGNED),
        CAST(JSON_UNQUOTE(JSON_EXTRACT(p_payload, '$.courseId')) AS UNSIGNED),
        JSON_UNQUOTE(JSON_EXTRACT(p_payload, '$.profilePictureName'))
      );
      SELECT LAST_INSERT_ID() AS new_roll;
    ELSEIF p_action = 'UPDATE' THEN
      UPDATE studenttable
        SET name = COALESCE(JSON_UNQUOTE(JSON_EXTRACT(p_payload, '$.name')), name),
            email = COALESCE(JSON_UNQUOTE(JSON_EXTRACT(p_payload, '$.email')), email),
            age = COALESCE(CAST(JSON_UNQUOTE(JSON_EXTRACT(p_payload, '$.age')) AS SIGNED), age),
            course_id = COALESCE(CAST(JSON_UNQUOTE(JSON_EXTRACT(p_payload, '$.courseId')) AS UNSIGNED), course_id),
            profile_picture_name = COALESCE(JSON_UNQUOTE(JSON_EXTRACT(p_payload, '$.profilePictureName')), profile_picture_name)
      WHERE roll = CAST(JSON_UNQUOTE(JSON_EXTRACT(p_payload, '$.roll')) AS UNSIGNED);
      SELECT CAST(JSON_UNQUOTE(JSON_EXTRACT(p_payload, '$.roll')) AS UNSIGNED) AS updated_roll;
    ELSEIF p_action = 'DELETE' THEN
      DELETE FROM studenttable
      WHERE roll = CAST(JSON_UNQUOTE(JSON_EXTRACT(p_payload, '$.roll')) AS UNSIGNED);
      SELECT CAST(JSON_UNQUOTE(JSON_EXTRACT(p_payload, '$.roll')) AS UNSIGNED) AS deleted_roll;
    ELSEIF p_action = 'GET' THEN
      SELECT s.roll, s.name, s.email, s.age,
             c.name AS course_name, s.course_id,
             s.profile_picture_name
      FROM studenttable s
      LEFT JOIN coursetable c ON s.course_id = c.id
      ORDER BY s.roll;
    ELSEIF p_action = 'GET_BY_COURSE' THEN
      SELECT s.roll, s.name, s.email, s.age,
             c.name AS course_name, s.course_id,
             s.profile_picture_name
      FROM studenttable s
      LEFT JOIN coursetable c ON s.course_id = c.id
      WHERE s.course_id = CAST(JSON_UNQUOTE(JSON_EXTRACT(p_payload, '$.courseId')) AS UNSIGNED)
      ORDER BY s.roll;
    END IF;

  -- =========================
        ---- TEACHER ----
  -- =========================
  ELSEIF p_entity = 'TEACHER' THEN
    IF p_action = 'ADD' THEN
      INSERT INTO teachertable(name, email, course_id, profile_picture_name, password)
      VALUES (
        JSON_UNQUOTE(JSON_EXTRACT(p_payload, '$.name')),
        JSON_UNQUOTE(JSON_EXTRACT(p_payload, '$.email')),
        CAST(JSON_UNQUOTE(JSON_EXTRACT(p_payload, '$.courseId')) AS UNSIGNED),
        JSON_UNQUOTE(JSON_EXTRACT(p_payload, '$.profilePictureName')),
        JSON_UNQUOTE(JSON_EXTRACT(p_payload, '$.password'))
      );
      SELECT LAST_INSERT_ID() AS new_id;
    ELSEIF p_action = 'UPDATE' THEN
      UPDATE teachertable
        SET name = COALESCE(JSON_UNQUOTE(JSON_EXTRACT(p_payload, '$.name')), name),
            email = COALESCE(JSON_UNQUOTE(JSON_EXTRACT(p_payload, '$.email')), email),
            course_id = COALESCE(CAST(JSON_UNQUOTE(JSON_EXTRACT(p_payload, '$.courseId')) AS UNSIGNED), course_id),
            profile_picture_name = COALESCE(JSON_UNQUOTE(JSON_EXTRACT(p_payload, '$.profilePictureName')), profile_picture_name),
            password = COALESCE(JSON_UNQUOTE(JSON_EXTRACT(p_payload, '$.password')), password)
      WHERE id = CAST(JSON_UNQUOTE(JSON_EXTRACT(p_payload, '$.id')) AS UNSIGNED);
      SELECT CAST(JSON_UNQUOTE(JSON_EXTRACT(p_payload, '$.id')) AS UNSIGNED) AS updated_id;
    ELSEIF p_action = 'DELETE' THEN
      DELETE FROM teachertable
      WHERE id = CAST(JSON_UNQUOTE(JSON_EXTRACT(p_payload, '$.id')) AS UNSIGNED);
      SELECT CAST(JSON_UNQUOTE(JSON_EXTRACT(p_payload, '$.id')) AS UNSIGNED) AS deleted_id;
    ELSEIF p_action = 'GET' THEN
      SELECT t.id, t.name, t.email,
             c.name AS course_name, t.course_id,
             t.profile_picture_name
      FROM teachertable t
      LEFT JOIN coursetable c ON t.course_id = c.id;
    ELSEIF p_action = 'LOGIN' THEN
      SELECT t.id, t.name, t.email, t.password,
             c.name AS course_name, t.course_id,
             t.profile_picture_name
      FROM teachertable t
      LEFT JOIN coursetable c ON t.course_id = c.id
      WHERE t.email = JSON_UNQUOTE(JSON_EXTRACT(p_payload, '$.email'));
    ELSEIF p_action = 'GET_BY_ID' THEN
      SELECT t.id, t.name, t.email,
             c.name AS course_name, t.course_id,
             t.profile_picture_name
      FROM teachertable t
      LEFT JOIN coursetable c ON t.course_id = c.id
      WHERE t.id = CAST(JSON_UNQUOTE(JSON_EXTRACT(p_payload, '$.id')) AS UNSIGNED);
    END IF;

  -- =========================
        ---- DOCUMENT ----
  -- =========================
  ELSEIF p_entity = 'DOCUMENT' THEN
    IF p_action = 'ADD' THEN
      INSERT INTO documenttable(student_roll, aadhar_name, pan_name, marksheet_name,
                               aadhar_uploaded, pan_uploaded, marksheet_uploaded)
      VALUES (
        CAST(JSON_UNQUOTE(JSON_EXTRACT(p_payload, '$.studentRoll')) AS UNSIGNED),
        JSON_UNQUOTE(JSON_EXTRACT(p_payload, '$.aadharName')),
        JSON_UNQUOTE(JSON_EXTRACT(p_payload, '$.panName')),
        JSON_UNQUOTE(JSON_EXTRACT(p_payload, '$.marksheetName')),
        IF(JSON_EXTRACT(p_payload, '$.aadharUploaded') = TRUE, 1, 0),
        IF(JSON_EXTRACT(p_payload, '$.panUploaded') = TRUE, 1, 0),
        IF(JSON_EXTRACT(p_payload, '$.marksheetUploaded') = TRUE, 1, 0)
      )
      ON DUPLICATE KEY UPDATE
        aadhar_name = VALUES(aadhar_name),
        pan_name = VALUES(pan_name),
        marksheet_name = VALUES(marksheet_name),
        aadhar_uploaded = VALUES(aadhar_uploaded),
        pan_uploaded = VALUES(pan_uploaded),
        marksheet_uploaded = VALUES(marksheet_uploaded),
        updated_at = CURRENT_TIMESTAMP;
      SELECT CAST(JSON_UNQUOTE(JSON_EXTRACT(p_payload, '$.studentRoll')) AS UNSIGNED) AS upsert_student_roll;
    ELSEIF p_action = 'UPDATE' THEN
      UPDATE documenttable
        SET aadhar_name = COALESCE(JSON_UNQUOTE(JSON_EXTRACT(p_payload, '$.aadharName')), aadhar_name),
            pan_name = COALESCE(JSON_UNQUOTE(JSON_EXTRACT(p_payload, '$.panName')), pan_name),
            marksheet_name = COALESCE(JSON_UNQUOTE(JSON_EXTRACT(p_payload, '$.marksheetName')), marksheet_name),
            aadhar_uploaded = COALESCE(IF(JSON_EXTRACT(p_payload, '$.aadharUploaded') = TRUE, 1, 0), aadhar_uploaded),
            pan_uploaded = COALESCE(IF(JSON_EXTRACT(p_payload, '$.panUploaded') = TRUE, 1, 0), pan_uploaded),
            marksheet_uploaded = COALESCE(IF(JSON_EXTRACT(p_payload, '$.marksheetUploaded') = TRUE, 1, 0), marksheet_uploaded)
      WHERE student_roll = CAST(JSON_UNQUOTE(JSON_EXTRACT(p_payload, '$.studentRoll')) AS UNSIGNED);
      SELECT CAST(JSON_UNQUOTE(JSON_EXTRACT(p_payload, '$.studentRoll')) AS UNSIGNED) AS updated_student_roll;
    ELSEIF p_action = 'DELETE' THEN
      DELETE FROM documenttable
      WHERE student_roll = CAST(JSON_UNQUOTE(JSON_EXTRACT(p_payload, '$.studentRoll')) AS UNSIGNED);
      SELECT CAST(JSON_UNQUOTE(JSON_EXTRACT(p_payload, '$.studentRoll')) AS UNSIGNED) AS deleted_student_roll;
    ELSEIF p_action = 'GET' THEN
      SELECT s.roll, s.name,
             d.aadhar_uploaded, d.pan_uploaded, d.marksheet_uploaded
      FROM studenttable s
      LEFT JOIN documenttable d ON d.student_roll = s.roll
      ORDER BY s.roll;
    END IF;
  END IF;
END$$
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

Vikash Kumar Singh
- [Telegram](https://t.me/encrypted_ghost) 
- [Mail](mailto:vikashkumarsingh8352@gmail.com)

Project Link: [https://github.com/ItsMeVikashKumarSingh/Student-Management.git](https://github.com/ItsMeVikashKumarSingh/Student-Management)
