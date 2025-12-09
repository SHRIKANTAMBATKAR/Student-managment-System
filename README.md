# Student-managment-System
A clean and efficient web application designed to manage student records, attendance, grades, and administrative tasks.
Built using Java, JSP, Servlets, JDBC, Bootstrap, and MySQL, this project demonstrates practical skills in full-stack web development and MVC architecture.

<div align="center">
🛠️ Tech Stack












</div>
🚀 Features
👤 Student Management

Add new students

Edit student information

Delete students

View all registered students

Clean, responsive data table

📝 Attendance Management

Mark attendance (Present / Absent)

Display last recorded attendance

Status badges

Auto-date entry using CURDATE()

🎓 Grades Management

Insert subject-wise marks

View grades list

Auto Grade Calculation (A+, A, B, C, D, F)

Beautiful grade dashboard UI

🔐 Authentication System

Secure admin login

Session-based validation

Role-based access

🖥️ Admin Dashboard

Modern responsive UI

Bootstrap cards

Navigation bar for all modules

Easy access to Students, Attendance, Grades

📂 Project Structure
StudentManagementSystem/
│
├── src/
│   └── shrikant/
│       ├── LoginServlet.java
│       ├── ViewStudentsServlet.java
│       ├── AttendanceServlet.java
│       ├── SaveAttendanceServlet.java
│       ├── GradesServlet.java
│       ├── DeleteStudentServlet.java
│       ├── UpdateProfileServlet.java
│       └── DBConnect.java
│
├── WebContent/ or webapp/
│   ├── index.html
│   ├── admin_dashboard.html
│   ├── register_student.html
│   ├── attendance_form.html
│   ├── grades.html
│   ├── style.css
│   └── screenshots/
│
└── README.md

🗄️ Database Schema
1. students table
Column	Type
id	INT (PK)
name	VARCHAR
email	VARCHAR
phone	VARCHAR
password	VARCHAR
branch	VARCHAR
year	INT
profile_pic	VARCHAR
2. attendance table
Column	Type
id	INT (PK)
student_id	INT (FK)
date	DATE
status	VARCHAR
3. grades table
Column	Type
id	INT (PK)
student_id	INT (FK)
subject	VARCHAR
marks	INT
▶️ How to Run the Project
Step 1: Clone the Repository
git clone https://github.com/SHRIKANTAMBATKAR/Student-managment-System.git

Step 2: Import into IDE

Open Eclipse / IntelliJ

Import as Dynamic Web Project

Step 3: Configure Database
CREATE DATABASE student_management;


Import tables

Update DBConnect.java with your MySQL credentials

Import tables using your schema

Update DBConnect.java with database credentials

Step 4: Run on Tomcat server

Right click project → Run As → Run on Server

Open browser:

http://localhost:8080/StudentManagementSystem/

📸 Screenshots (Add your images)

You can upload screenshots inside a folder and link them:
!(![Login and Registration](https://github.com/user-attachments/assets/97d4ec02-341d-4d99-b1a3-abf1c186cf1f)

![Admin Dashbord and StudentDashbord](https://github.com/user-attachments/assets/d03625a0-311d-48a8-8967-19a6c717d9e5)

<img width="800" height="487" alt="Screenshot 2025-12-09 221057" src="https://github.com/user-attachments/assets/2bf91baa-7334-4803-a52e-211a0d5d567e" />

🤝 Contributions

Pull requests are welcome.
For major updates, open an issue first to discuss the change.
