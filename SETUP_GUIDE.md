# Setup Guide - How to Run This Thing

Hey, here is how to get the project running on your laptop. It's pretty straightforward since there's no SQL server to install.

## 1. Prerequisites
Make sure you have:
* **Java JDK:** Version 8 or newer (I used 17, but 8 works too).
* **IDE:** IntelliJ IDEA, NetBeans, Eclipse, or VS Code with the Java Extension (I used this).

## 2. Installation
1.  **Download/Clone** this repo.
2.  Open your IDE and **Import Project** (point it to this folder).
3.  Make sure the src folder is marked as the "Sources Root".

## 3. The Database (Important!)
You **DO NOT** need to create any files manually.
* When you run the app for the first time, the DatabaseHelper.java class will automatically create a folder called database/ in your project root.
* It will create all the necessary text files (users.txt, matches.txt, etc.) and folders for resumes/contracts.

**Note:** If the app crashes on the very first run, just run it again. Sometimes it needs a second to create the folders.

## 4. Running the App
* Find the file: src/common/LoginUI.java
* Right-click -> **Run File** (or press Shift+F6 in NetBeans / Ctrl+Shift+F10 in IntelliJ).

## 5. Default Login Credentials
Since the database is empty at first, use the hardcoded Admin account to get started and create other users.

**Admin Account**
* **Username:** admin
* **Password:** admin123

### How to set up test users:
1.  Log in as **Admin**.
2.  Go to **"Register & Manage User Account"**.
3.  Create a **Student** account.
4.  Create a **Company Supervisor** account.
5.  Create an **Academic Supervisor** account.
6.  Log out and test them!

## Troubleshooting

**"I can't find the resume file!"**
* Make sure you actually uploaded one in the Student Profile screen. The system saves them in database/resume/.

**"The tables are empty?"**
* You probably haven't added any data yet. Log in as Admin or Company Supervisor to add listings or users.

**"The UI looks weird on my screen."**
* I used setLocationRelativeTo(null) to center windows, but if you have high DPI scaling, it might look tiny. Sorry about that!

**"I manually edited users.txt and now it crashes."**
* Be careful editing the text files directly! The code expects commas to separate data. If you messed it up, just delete the line or fix the commas.

---
*Good luck running it!*