# 🏥 Hospital Management System (Java + MySQL)

This is a **console-based Hospital Management System** project built using **Java (JDK 21)** and **MySQL**.  
The system allows you to manage patients, doctors, and appointments in a hospital database.

---

## 🚀 Features
- ➕ Add new patients with details (name, age, gender).
- 👀 View all patients.
- 👩‍⚕️ View doctors and their specializations.
- 📅 Book appointments between patients and doctors.
- ✅ Check doctor availability before booking.
- 🔒 Secure database connection using **MySQL JDBC driver**.

---

## ⚙️ Tech Stack
- **Programming Language:** Java (JDK 21)
- **Database:** MySQL
- **Libraries:** MySQL Connector/J
- **IDE Used:** IntelliJ IDEA
---
## 🛠️ Database Setup

Run the following SQL commands to set up the database:

```sql
CREATE DATABASE hospital;
USE hospital;

-- Patient Table
CREATE TABLE patient (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100),
    age INT,
    gender VARCHAR(20)
);

-- Doctors Table
CREATE TABLE doctors (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100),
    specialization VARCHAR(50)
);

-- Appointments Table
CREATE TABLE appointments (
    id INT AUTO_INCREMENT PRIMARY KEY,
    patient_id INT,
    docter_id INT,
    appointment_date DATE,
    FOREIGN KEY (patient_id) REFERENCES patient(id),
    FOREIGN KEY (docter_id) REFERENCES doctors(id)
);

▶️ How to Run

Clone or download the project.

Open in IntelliJ IDEA.

Add MySQL Connector/J JAR:

File → Project Structure → Modules → Dependencies → + JARs or Directories

Select mysql-connector-j-9.4.0.jar

Update your MySQL username and password in
HospitalManagementSystem.java:

private static final String username = "root";
private static final String password = "your_password";


Run the program.

🖥️ Menu Options

When you run the project, the following menu appears:

Hospital Management System
1. Add Patient
2. View Patient
3. View Doctor
4. Book Appointment
5. Exit
Enter your choice:

📸 Sample Output
➕ Adding Patient
Enter Patient Name:
Kishan Kushava
Enter Patient Age:
24
Enter Patient Gender:
Male
Patient has been successfully added

👩‍⚕️ Viewing Doctors
Doctors details:
+------------+-------------+----------------+
| Doctor Id  | Name        | Specialization |
+------------+-------------+----------------+
| 1          | Kishan      | Physician      |
| 2          | Anil Khanna | NeuroSurgeon   |
+------------+-------------+----------------+

📅 Booking Appointment
Enter Patient ID:
1
Enter Doctor ID:
2
Enter appointment date (yyyy-mm-dd):
2025-09-20
Appointment has been added successfully

🔮 Future Enhancements

🔑 Admin login system.

✏️ Update/Delete patient and doctor records.

🖥️ GUI-based version using JavaFX or Swing.

📊 Generate reports (patients, doctors, appointments).

👨‍💻 Author

Kishan Kushavaha
🎓 Student | 💻 Developer | 🚀 Enthusiastic Learner
