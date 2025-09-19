# Student Management System

<div align="center">
  <img src="docs/images/student_management_logo.png" alt="Student Management System" style="max-width: 100%; width: 350px; height: auto;">
</div>

[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT)
[![Java](https://img.shields.io/badge/Java-17-orange)](https://www.java.com/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.0-green)](https://spring.io/projects/spring-boot)
[![MySQL](https://img.shields.io/badge/MySQL-8.0-blue)](https://www.mysql.com/)

A comprehensive enterprise-level full-stack web application for managing students, teachers, courses, and documents in an educational setting. Features session-based authentication systems, document management, profile handling, role-based access control, and real-time dashboards. Built with Spring Boot backend and modern responsive frontend with professional UI components.

This project demonstrates enterprise-level Spring Boot concepts including JPA with stored procedures, Spring Security with session management, comprehensive file upload handling, RESTful APIs, document management systems, and professional responsive UI with Bootstrap and AG Grid.

## Features

### 🔐 Authentication \& Authorization

* **Multi-Role System**: Admin, User, and Teacher roles with granular permissions
* **Session-Based Authentication**: Secure session management with Spring Security
* **In-Memory Authentication**: Admin and User roles with configurable credentials
* **Teacher Database Authentication**: Teachers authenticate using email/password stored in database
* **Session Management**: Automatic session timeout and security features
* **Role-Based Access Control**: Protected endpoints based on user roles


### 👥 Comprehensive User Management

* **Student Management**: Complete CRUD with profile pictures, course assignments, and document tracking
* **Teacher Management**: Teacher profiles with authentication, course assignments, and personalized dashboards
* **Course Management**: Advanced course creation with teacher and student assignments
* **Document Management**: Complete document lifecycle management for student records
* **Profile Pictures**: Upload, serve, and manage profile images for all user types


### 📄 Document Management System

* **Document Upload**: Secure upload system for Aadhar, PAN, Marksheet, and other documents
* **Document Tracking**: Real-time status tracking of document uploads and verification
* **Document Serving**: Secure document serving with access controls
* **PDF Viewer**: Built-in PDF viewing capabilities
* **Storage Management**: Organized file storage with automatic directory management


### 🖼️ Advanced File \& Media Handling

* **Profile Picture Management**: Upload, resize, and serve profile pictures
* **Document Storage**: Secure document storage with organized directory structure
* **File Validation**: Comprehensive file type, size, and security validation
* **Image Serving**: Dynamic image serving with caching and optimization
* **Storage Services**: Dedicated storage services for different file types


### 📊 Professional Dashboards \& UI

* **Admin Dashboard**: Comprehensive management interface with analytics
* **Teacher Dashboard**: Personalized dashboard showing assigned students, courses, and analytics
* **Student Portal**: Student-specific interface for document management
* **Document Dashboard**: Specialized interface for document management and tracking
* **Responsive Design**: Mobile-first responsive design with Bootstrap 5
* **Advanced Data Grids**: AG Grid with search, filter, sort, pagination, and export
* **Real-time Updates**: Dynamic content updates with AJAX


### 🔧 Enterprise Technical Features

* **Advanced Stored Procedures**: Complex MySQL stored procedures with JSON payload handling
* **RESTful API Design**: Comprehensive REST API with proper HTTP status codes and error handling
* **Session Management**: Secure session handling with timeout and validation
* **File Upload Security**: Advanced file upload security with validation and sanitization
* **Login Management**: Sophisticated login tracking and session management
* **Export Functionality**: Advanced data export capabilities with multiple formats


## Tech Stack

### Backend Technologies

* **Java 17** - Latest LTS version with modern features
* **Spring Boot 3.x** - Main application framework
* **Spring Security** - Authentication, authorization, and session management
* **Spring Data JPA** - Database operations with advanced Hibernate features
* **HttpSession** - Session-based authentication and state management
* **MySQL 8.x** - Primary database with advanced stored procedures
* **Maven** - Build automation and dependency management


### Frontend Technologies

* **HTML5 \& CSS3** - Modern semantic markup and styling
* **Bootstrap 5** - Professional responsive CSS framework
* **JavaScript (ES6+)** - Modern JavaScript with async/await
* **jQuery** - Enhanced DOM manipulation and AJAX operations
* **AG Grid Community** - Enterprise-level data grid with advanced features
* **Font Awesome 6** - Comprehensive icon library


### File \& Storage Management

* **MultipartFile Handling** - Advanced Spring Boot file upload processing
* **Organized File Storage** - Structured external storage system
* **Document Management** - Complete document lifecycle management
* **Image Processing** - Dynamic image serving and optimization
* **Security Validation** - File type and content validation


## Installation

### Prerequisites

* **Java 17+** (JDK) - Required for Spring Boot 3.x
* **Maven 3.8+** - Build automation tool
* **MySQL 8.0+** - Primary database server
* **Git** - Version control


### Setup Steps

1. **Clone the Repository**:
```bash
git clone https://github.com/ItsMeVikashKumarSingh/Student-Management.git
cd Student-Management
```

2. **Configure Application** (Edit `src/main/resources/application.properties`):
```properties
# Database Configuration
spring.datasource.url=jdbc:mysql://localhost:3306/studentdb?useSSL=false&serverTimezone=UTC
spring.datasource.username=root
spring.datasource.password=yourpassword
spring.jpa.hibernate.ddl-auto=none
spring.jpa.show-sql=true

# File Upload Configuration
spring.servlet.multipart.max-file-size=10MB
spring.servlet.multipart.max-request-size=10MB
spring.servlet.multipart.enabled=true

# Session Configuration
server.servlet.session.timeout=30m
spring.session.store-type=none
spring.security.require-ssl=false

# Server Configuration
server.port=8080
```

3. **Create Upload Directories**:
```bash
mkdir -p uploads/image/StudentPic
mkdir -p uploads/image/TeacherPic
mkdir -p uploads/image/docs
```

4. **Build and Run**:
```bash
mvn clean install
mvn spring-boot:run
```

5. **Access Application**:

* Main Login: `http://localhost:8080/login.html`
* Admin Dashboard: `http://localhost:8080/dashboard.html` (after login)
* Teacher Dashboard: `http://localhost:8080/teacher-dashboard.html` (after teacher login)


## Database Setup

[Keep the same database setup section from the previous README with tables and stored procedure]

## API Endpoints

### Authentication Endpoints

```
POST   /teachers/login          # Teacher authentication (email/password from database)
POST   /teachers/logout         # Teacher logout and session cleanup
GET    /teachers/dashboard      # Teacher dashboard data with student list
POST   /teachers/generate-password # Generate secure password for teachers
```


### Student Management

```
GET    /students/all            # Get all students with course and profile info
POST   /students/add            # Add new student with profile picture upload
PUT    /students/update         # Update student information and profile
DELETE /students/delete/{roll}  # Delete student and associated documents
```


### Teacher Management

```
GET    /teachers/all            # Get all teachers with course assignments
POST   /teachers/add            # Add new teacher with authentication setup
PUT    /teachers/update         # Update teacher information and profile
DELETE /teachers/delete/{id}    # Delete teacher account
```


### Course Management

```
GET    /courses/all            # Get all courses with enrollment counts
POST   /courses/add            # Create new course
PUT    /courses/update         # Update course information
DELETE /courses/delete/{id}    # Delete course (if no dependencies)
GET    /courses/suggestions    # Get course data for autocomplete
```


### Document Management

```
GET    /documents/all          # Get document status for all students
POST   /documents/upload       # Upload student documents (Aadhar, PAN, Marksheet)
PUT    /documents/update       # Update document information
GET    /documents/student/{id} # Get documents for specific student
```


### File Serving

```
GET    /image/StudentPic/{filename}  # Serve student profile pictures
GET    /image/TeacherPic/{filename}  # Serve teacher profile pictures
GET    /image/docs/{studentId}/{filename} # Serve student documents securely
```


## Usage

### Default Login Credentials

**Admin Access** (Full Management):

- Username: `admin`
- Password: `password`
- Access: Complete CRUD operations on all entities, user management, system configuration

**User Access** (Read-Only):

- Username: `user`
- Password: `password`
- Access: View-only access to students, teachers, and courses data

**Teacher Access** (Course-Specific):

- Create through admin panel with email/password
- Login using email and password stored in database
- Access: Personal dashboard, assigned student management, document tracking


### Key Features Usage

1. **Student Management**:
    - Upload and manage profile pictures
    - Assign students to courses via dropdown
    - Track document upload status
    - Export student data to CSV
2. **Teacher Dashboard**:
    - View assigned students with course information
    - Track student document completion
    - Export class lists and reports
    - Manage personal profile and course details
3. **Document System**:
    - Upload Aadhar, PAN, and Marksheet documents
    - Track document verification status
    - View and download uploaded documents
    - Organize documents by student
4. **Advanced Features**:
    - Real-time search across all data grids
    - Advanced filtering and sorting
    - Bulk data export functionality
    - Responsive design for mobile devices
    - Secure file access with session authentication

## Screenshots

### Authentication \& Dashboards

*Login interface supporting admin, user, and teacher authentication*

*Comprehensive admin dashboard with system overview and management tools*

*Personalized teacher dashboard showing assigned students and course information*

### Management Interfaces

*Student management interface with profile pictures and course assignments*

*Teacher management system with authentication and course assignment features*

*Course management interface with enrollment tracking and teacher assignments*

### Document \& File Management

*Document management system with upload tracking and status monitoring*

*Document upload interface for student records (Aadhar, PAN, Marksheet)*

*Profile picture upload and management system*

### Advanced Features

*Advanced data export functionality with filtering and format options*

## Contributing

We welcome contributions to improve the Student Management System! Here's how you can contribute:

### Getting Started

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/AmazingFeature`)
3. Make your changes
4. Commit your changes (`git commit -m 'Add some AmazingFeature'`)
5. Push to the branch (`git push origin feature/AmazingFeature`)
6. Open a Pull Request

### Contribution Guidelines

- **Code Style**: Follow Java coding conventions and Spring Boot best practices
- **Testing**: Add appropriate unit and integration tests for new features
- **Documentation**: Update README and API documentation for any changes
- **UI/UX**: Ensure responsive design and accessibility for frontend changes
- **Security**: Follow security best practices, especially for authentication and file handling


### Areas for Contribution

- JWT Authentication implementation
- Enhanced reporting and analytics features
- Mobile application development
- Advanced document processing capabilities
- Integration with external authentication providers
- Performance optimization and caching
- Additional export formats and reporting tools


## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## Contact

**Vikash Kumar Singh**

- 📱 [Telegram](https://t.me/encrypted_ghost)
- 📧 [Email](mailto:vikashkumarsingh8352@gmail.com)
- 💻 [GitHub](https://github.com/ItsMeVikashKumarSingh)

**Project Repository:** [https://github.com/ItsMeVikashKumarSingh/Student-Management](https://github.com/ItsMeVikashKumarSingh/Student-Management)

***

## Version History \& Updates

### Version 2.0 (Current) - Enhanced Features

- ✅ **Session-Based Authentication** with Spring Security
- ✅ **Advanced Document Management** with PDF viewer and organized storage
- ✅ **Teacher Authentication System** with database-stored credentials
- ✅ **Enhanced File Storage Services** with security validation
- ✅ **Professional UI Improvements** with modern responsive design
- ✅ **Advanced Profile Management** for students and teachers
- ✅ **Comprehensive API Documentation** with detailed endpoint descriptions
- ✅ **Enhanced Security Features** with role-based access control
- ✅ **Document Upload Tracking** with real-time status updates
- ✅ **Advanced Export Capabilities** with multiple format support


### Upcoming Features (Roadmap)

- [ ] **JWT Authentication System** for stateless authentication
***

