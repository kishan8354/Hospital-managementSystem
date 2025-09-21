package HospitalManagementSystem;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class Patient {
    private Connection connection;
    private Scanner scanner;

    public Patient(Connection connection, Scanner scanner) {
        this.connection = connection;
        this.scanner = scanner;
    }

    // ==================== CREATE ====================
    public void addPatient() {
        System.out.print("Enter Patient Name: ");
        String name = scanner.next();
        System.out.print("Enter Patient Age: ");
        int age = scanner.nextInt();
        System.out.print("Enter Patient Gender: ");
        String gender = scanner.next();

        String query = "INSERT INTO patients(name, age, gender) VALUES (?, ?, ?)";

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, name);
            preparedStatement.setInt(2, age);
            preparedStatement.setString(3, gender);

            int affectedRows = preparedStatement.executeUpdate();
            if (affectedRows > 0) {
                System.out.println("Patient has been added successfully!");
            } else {
                System.out.println("Failed to add patient.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // ==================== READ ====================
    public void viewPatients() {
        String query = "SELECT * FROM patients";

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            ResultSet resultSet = preparedStatement.executeQuery();

            System.out.println("Patient details:");
            System.out.println("+-------------+--------------+------+--------+");
            System.out.println("| Patient Id  | Name         | Age  | Gender |");
            System.out.println("+-------------+--------------+------+--------+");

            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                int age = resultSet.getInt("age");
                String gender = resultSet.getString("gender");

                System.out.printf("|%-12s|%-14s|%-6s|%-8s|\n", id, name, age, gender);
                System.out.println("+-------------+--------------+------+--------+");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean getPatientById(int id) {
        String query = "SELECT * FROM patients WHERE id=?";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            return resultSet.next();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // ==================== UPDATE ====================
    public void updatePatient(int id) {
        if (!getPatientById(id)) {
            System.out.println("Patient with id " + id + " does not exist!");
            return;
        }

        System.out.print("Enter new Patient Name: ");
        String name = scanner.next();
        System.out.print("Enter new Patient Age: ");
        int age = scanner.nextInt();
        System.out.print("Enter new Patient Gender: ");
        String gender = scanner.next();

        String query = "UPDATE patients SET name=?, age=?, gender=? WHERE id=?";

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, name);
            preparedStatement.setInt(2, age);
            preparedStatement.setString(3, gender);
            preparedStatement.setInt(4, id);

            int rowsUpdated = preparedStatement.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("Patient updated successfully!");
            } else {
                System.out.println("Failed to update patient.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // ==================== DELETE ====================
    public void deletePatient(int id) {
        if (!getPatientById(id)) {
            System.out.println("Patient with id " + id + " does not exist!");
            return;
        }

        String query = "DELETE FROM patients WHERE id=?";

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, id);

            int rowsDeleted = preparedStatement.executeUpdate();
            if (rowsDeleted > 0) {
                System.out.println("Patient deleted successfully!");
            } else {
                System.out.println("Failed to delete patient.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

