package HospitalManagementSystem;
import java.sql.*;
import java.util.Scanner;

public class HospitalManagementSystem {
    private static final String url = "jdbc:mysql://localhost:3306/hospital";
    private static final String username = "root";
    private static final String password = "9198";

    public static void main(String[] args) {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver"); // Load JDBC driver
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        try (Scanner scanner = new Scanner(System.in);
             Connection connection = DriverManager.getConnection(url, username, password)) {

            Patient patient = new Patient(connection, scanner);
            Doctors doctor = new Doctors(connection);

            while (true) {
                System.out.println("\n=== Hospital Management System ===");
                System.out.println("1. Add Patient");
                System.out.println("2. View Patients");
                System.out.println("3. Update Patient");
                System.out.println("4. Delete Patient");
                System.out.println("5. Add Doctor");
                System.out.println("6. View Doctors");
                System.out.println("7. Update Doctor");
                System.out.println("8. Delete Doctor");
                System.out.println("9. Book Appointment");
                System.out.println("10. View Appointments");
                System.out.println("11. Update Appointment");
                System.out.println("12. Delete Appointment");
                System.out.println("13. Exit");
                System.out.print("Enter your choice: ");

                int choice = scanner.nextInt();
                switch (choice) {
                    // ==== PATIENT CRUD ====
                    case 1 -> patient.addPatient();
                    case 2 -> patient.viewPatients();
                    case 3 -> {
                        System.out.print("Enter Patient ID to update: ");
                        int id = scanner.nextInt();
                        patient.updatePatient(id);
                    }
                    case 4 -> {
                        System.out.print("Enter Patient ID to delete: ");
                        int id = scanner.nextInt();
                        patient.deletePatient(id);
                    }

                    // ==== DOCTOR CRUD ====
                    case 5 -> doctor.addDoctor();
                    case 6 -> doctor.viewDoctors();
                    case 7 -> {
                        System.out.print("Enter Doctor ID to update: ");
                        int id = scanner.nextInt();
                        doctor.updateDoctor(id);
                    }
                    case 8 -> {
                        System.out.print("Enter Doctor ID to delete: ");
                        int id = scanner.nextInt();
                        doctor.deleteDoctor(id);
                    }

                    // ==== APPOINTMENTS ====
                    case 9 -> bookAppointment(patient, doctor, connection, scanner);
                    case 10 -> viewAppointments(connection);
                    case 11 -> updateAppointment(connection, scanner, patient, doctor);
                    case 12 -> deleteAppointment(connection, scanner);
                    case 13 -> {
                        System.out.println("Exiting...");
                        return;
                    }
                    default -> System.out.println("Invalid choice! Try again.");
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // =================== APPOINTMENT METHODS ===================
    public static void bookAppointment(Patient patient, Doctors doctor, Connection connection, Scanner scanner) {
        System.out.print("Enter Patient ID: ");
        int patientID = scanner.nextInt();
        System.out.print("Enter Doctor ID: ");
        int doctorID = scanner.nextInt();
        System.out.print("Enter appointment date (yyyy-mm-dd): ");
        String appointmentDate = scanner.next();

        if (patient.getPatientById(patientID) && doctor.getDoctorById(doctorID)) {
            if (checkDoctorAvailability(doctorID, appointmentDate, connection)) {
                String query = "INSERT INTO appointments(patient_id, docter_id, appointment_date) VALUES (?, ?, ?)";
                try {
                    PreparedStatement ps = connection.prepareStatement(query);
                    ps.setInt(1, patientID);
                    ps.setInt(2, doctorID);
                    ps.setString(3, appointmentDate);
                    int rows = ps.executeUpdate();
                    if (rows > 0) System.out.println("Appointment booked successfully!");
                    else System.out.println("Failed to book appointment.");
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            } else {
                System.out.println("Doctor is not available on this date. Choose another date.");
            }
        } else {
            System.out.println("Invalid Patient ID or Doctor ID.");
        }
    }

    public static boolean checkDoctorAvailability(int doctorID, String appointmentDate, Connection connection) {
        String query = "SELECT COUNT(*) FROM appointments WHERE docter_id=? AND appointment_date=?";
        try {
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setInt(1, doctorID);
            ps.setString(2, appointmentDate);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return rs.getInt(1) == 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static void viewAppointments(Connection connection) {
        String query = """
                SELECT a.id, p.name AS patient_name, d.name AS doctor_name, a.appointment_date
                FROM appointments a
                JOIN patients p ON a.patient_id = p.id
                JOIN doctors d ON a.docter_id = d.id
                """;
        try {
            PreparedStatement ps = connection.prepareStatement(query);
            ResultSet rs = ps.executeQuery();
            System.out.println("+----+----------------+----------------+----------------+");
            System.out.println("| ID | Patient Name    | Doctor Name     | Appointment Date|");
            System.out.println("+----+----------------+----------------+----------------+");
            while (rs.next()) {
                int id = rs.getInt("id");
                String patient = rs.getString("patient_name");
                String doctor = rs.getString("doctor_name");
                String date = rs.getString("appointment_date");
                System.out.printf("|%-4s|%-16s|%-16s|%-16s|\n", id, patient, doctor, date);
            }
            System.out.println("+----+----------------+----------------+----------------+");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void updateAppointment(Connection connection, Scanner scanner, Patient patient, Doctors doctor) {
        System.out.print("Enter Appointment ID to update: ");
        int appointmentID = scanner.nextInt();
        System.out.print("Enter new Patient ID: ");
        int patientID = scanner.nextInt();
        System.out.print("Enter new Doctor ID: ");
        int doctorID = scanner.nextInt();
        System.out.print("Enter new appointment date (yyyy-mm-dd): ");
        String appointmentDate = scanner.next();

        try {
            String query = "UPDATE appointments SET patient_id=?, docter_id=?, appointment_date=? WHERE id=?";
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setInt(1, patientID);
            ps.setInt(2, doctorID);
            ps.setString(3, appointmentDate);
            ps.setInt(4, appointmentID);
            int rows = ps.executeUpdate();
            if (rows > 0) System.out.println("Appointment updated successfully!");
            else System.out.println("Failed to update appointment.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void deleteAppointment(Connection connection, Scanner scanner) {
        System.out.print("Enter Appointment ID to delete: ");
        int appointmentID = scanner.nextInt();

        try {
            String query = "DELETE FROM appointments WHERE id=?";
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setInt(1, appointmentID);
            int rows = ps.executeUpdate();
            if (rows > 0) System.out.println("Appointment deleted successfully!");
            else System.out.println("Failed to delete appointment.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

