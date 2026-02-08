# Internship Management System (Java Swing)

This is a project for managing the university internship process. Basically, it connects Students, Company Supervisors, and Academic Supervisors all in one app so we don't have to use a million spreadsheets.

It's built with **Java Swing** for the GUI and uses a **Text File Database** (CSV style).

## What it does

The system handles the whole flow from a student applying to a job to them getting graded at the end.

### User Roles & Features

**1. Student**
* **Profile:** Update details, upload resume (PDF), and set a profile pic.
* **Job Board:** Browse approved internships and apply.
* **Tracker:** See if you got an offer or got rejected (RIP).
* **Daily Tasks:** Clock in/out for attendance and fill in your daily logbook.
* **Progress:** See your completion bars and feedback.

**2. Company Supervisor**
* **Jobs:** Post new internship listings (needs Admin approval).
* **Hiring:** Review applicants, view resumes, and send offer letters (contracts).
* **Management:** Verify student logbooks and attendance records.
* **Grading:** Give the student a final score and feedback at the end.

**3. Academic Supervisor**
* **Monitoring:** See which students are assigned to you.
* **Review:** Check student logbooks to make sure they aren't slacking.
* **Grading:** Submit the academic side of the evaluation.
* **Dashboard:** See visual graphs of how your students are doing.

**4. Admin**
* **God Mode:** Add/Edit users (Students, Lecturers, Company folks).
* **Matchmaking:** Manually link a student to a lecturer.
* **Analytics:** See charts on how many students are placed vs. unplaced.
* **Broadcast:** Send system-wide notifications to everyone.

## Tech Stack
* **Language:** Java (JDK 8 or higher).
* **GUI:** Java Swing (JFrame, JPanel, the usual).
* **Database:** .txt files (Custom CSV parsing in DatabaseHelper.java).
* **Assets:** Stores images and PDFs in a local folder.

## Project Structure
* src/admin - All the admin screens.
* src/student - Student screens.
* src/company - Company screens.
* src/academic - Lecturer screens.
* src/common - Shared stuff (Login, Database helper, Notifications).
* database/ - Where the magic happens (all the data files live here).

---
*Created by Ahmad Danish Bin Syeriffudin, Group 5, TT5L*