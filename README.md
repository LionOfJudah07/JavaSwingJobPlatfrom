# Job Matchmaking and Management Platform

A multi-tier Java Swing desktop application designed to handle recruitment processes, job postings, application pipelines, and administrative controls. The system relies on local text-file repositories for efficient runtime data persistence.

---

## 🚀 Architectural Features

### 1. Robust Access Levels
* **Admin Dashboard:** Control system health by activating/deactivating compromised corporate profiles or job seeker accounts, monitoring platform-wide job listings, and enforcing global password resets.
* **Company Space:** Empower recruiters to post structured job specs (with quantitative salary formats, location tags, and requirements), track and update active applicant streams, and coordinate upcoming interviews.
* **Job Seeker Module:** A clean application pipeline that allows candidates to view open positions, submit application forms, trace tracking history, manage personal portfolio configurations, or safely terminate active profiles.

### 2. Built-in Security & Data Verification
* Strong input syntax checking for crucial fields like email addresses (`@` and `.` character validation) and strict digit-only phone rules.
* Front-end verification rules that block fragile parameters (e.g., usernames shorter than 4 characters or passwords under 6 characters).
* Safe input processing with `try-catch` structures that stop bad user inputs from causing system-wide application failures.

### 3. File System Synchronization
* Auto-saving data arrays that sync directly into structured local flat-files (`companies.txt`, `seekers.txt`, `jobs.txt`, `applications.txt`, `admins.txt`) when important updates are made or when exiting the application.

---

## 🛠️ Requirements & Setup

### Prerequisites
* **Java Development Kit (JDK):** Version 8 or higher.
* **IDE/Build Tool:** IntelliJ IDEA, Eclipse, NetBeans, or a standalone Command Line Compiler.

### Installation & Execution
1. Clone the project or copy the raw `.java` files into your package directories.
2. Ensure your directory layout matches your project package declarations:
   
```text
   src/
   └── exceptionHandling/
       └── JobPlatformGUI.java