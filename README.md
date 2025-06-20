
# 🎓 Course Management Application

A **JavaFX-based mock university enrollment system** with an integrated **SQLite database**. This Course Management System (CMS) allows users to register, enroll in courses, generate timetables, and manage their academic profile through a desktop application.

---

## ✨ Features

- **🔐 User Signup & Profile Management**
  - Create accounts with secure login
  - Auto-generated unique student numbers
  - Edit profile details (except student ID)

- **📚 Course Enrollment**
  - View available courses
  - Double-click to enroll
  - Built-in timetable clash detection

- **📅 Timetable Generation**
  - Auto-generated weekly view of enrolled courses

- **📄 Export Functionality**
  - Export list of enrolled courses to a `.txt` file

- **📂 SQLite Database**
  - Stores students, courses, enrollments
  - Persists data between sessions
  - Encrypted user info for security

- **📃 Course Management**
  - View all available courses
  - Enroll or unenroll at any time

---

## 🏗️ Tech Stack

- **Java 8+**
- **JavaFX (FXML)**
- **SQLite (via JDBC)**
- **IDE**: IntelliJ IDEA / Eclipse

---

## 🗂️ Folder Structure

```bash
CourseManagementApplication/
├── src/
│   ├── controllers/
│   ├── models/
│   ├── views/
│   └── DBConfig.java
├── resources/
│   ├── exported_courses.txt
│   └── course_list.fxml
├── cms.db
└── README.md
```

---

## 🔧 Installation & Setup

### 1. Clone the Repository


git clone https://github.com/Brum99/CourseManagementApplication.git


### 2. Open in IDE

* Use IntelliJ IDEA or Eclipse
* Import the project as a Maven/Gradle/Java project

### 3. Configure JavaFX

* Add JavaFX SDK to your project libraries
* Set VM options in run configuration (if needed):


--module-path /path/to/javafx-sdk/lib --add-modules javafx.controls,javafx.fxml


### 4. Configure the Database

* Ensure `cms.db` is created in your project directory
* Run the provided SQL schema (or use the app to auto-initialize)
* Update database path if needed in `DBConfig.java`

---

## 🧪 Usage Instructions

1. **Run the application**
2. **Create a user profile**
3. **Browse and enroll in courses**
4. **View your weekly timetable**
5. **Export your enrolled courses list**
6. **Withdraw from courses when needed**

---

## 🧑‍🏫 Sample Workflow

1. **Signup →** John Doe → `johndoe123`
2. **Enroll →** "CS101", "MATH203"
3. **View Timetable →** Weekly calendar auto-filled
4. **Export →** `resources/exported_courses.txt`
5. **Withdraw →** Drop "MATH203"

---

## 🤝 Contributing

Contributions are welcome!
To contribute:

* Fork this repo
* Create a new branch
* Make your changes
* Submit a pull request

---

## 📢 Contact

For questions or issues, please reach out:

📧 [samdpeterson1999@gmail.com](mailto:samdpeterson1999@gmail.com)


---

