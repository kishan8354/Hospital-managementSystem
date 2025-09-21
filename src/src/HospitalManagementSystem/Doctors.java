package HospitalManagementSystem;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class Doctors {
    private Connection connection;
    private Scanner scanner;

    public Doctors(Connection connection) {
        this.connection = connection;
        this.scanner = new Scanner(System.in);
    }

    // ==================== CREATE ====================
    public void addDoctor() {
        System.out.print("Enter Doctor Name: ");
        String name = scanner.nextLine();
        System.out.print("Enter Specialization: ");
        String specialization = scanner.nextLine();

        String query = "INSERT INTO doctors (name, specialization) VALUES (?, ?)";

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, name);
            preparedStatement.setString(2, specialization);
            int rowsInserted = preparedStatement.executeUpdate();
            if (rowsInserted > 0) {
                System.out.println("Doctor added successfully!");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // ==================== READ ====================
    public void viewDoctors() {
        String query = "SELECT * FROM doctors";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            ResultSet resultSet = preparedStatement.executeQuery();

            System.out.println("Doctors details:");
            System.out.println("+------------+-------------+----------------+");
            System.out.println("| Doctor Id  | Name        | Specialization |");
            System.out.println("+------------+-------------+----------------+");

            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                String specialization = resultSet.getString("specialization");
                System.out.printf("|%-12s|%-13s|%-16s|\n", id, name, specialization);
                System.out.println("+------------+-------------+----------------+");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean getDoctorById(int id) {
        String query = "SELECT * FROM doctors WHERE id=?";
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
    public void updateDoctor(int id) {
        if (!getDoctorById(id)) {
            System.out.println("Doctor with id " + id + " does not exist!");
            return;
        }

        System.out.print("Enter new Doctor Name: ");
        String name = scanner.nextLine();
        System.out.print("Enter new Specialization: ");
        String specialization = scanner.nextLine();

        String query = "UPDATE doctors SET name=?, specialization=? WHERE id=?";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, name);
            preparedStatement.setString(2, specialization);
            preparedStatement.setInt(3, id);
            int rowsUpdated = preparedStatement.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("Doctor updated successfully!");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // ==================== DELETE ====================
    public void deleteDoctor(int id) {
        if (!getDoctorById(id)) {
            System.out.println("Doctor with id " + id + " does not exist!");
            return;
        }

        String query = "DELETE FROM doctors WHERE id=?";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, id);
            int rowsDeleted = preparedStatement.executeUpdate();
            if (rowsDeleted > 0) {
                System.out.println("Doctor deleted successfully!");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
