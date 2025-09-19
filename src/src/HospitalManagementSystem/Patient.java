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
    public void addPatient() {
        System.out.println("Enter Patient Name: ");
        String name=scanner.next();
        System.out.println("Enter Patient Age: ");
        int age =scanner.nextInt();
        System.out.println("Enter Patient Gender: ");
        String gender=scanner.next();
        try{
            String query = "INSERT INTO patients(name,age,gender) VALUES ( ?, ?, ?)";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, name);
            preparedStatement.setInt(2, age);
            preparedStatement.setString(3, gender);
            int affectedRows=   preparedStatement.executeUpdate();
            if(affectedRows>0){
                System.out.println("Patient has been added successfully");
            }else{
                System.out.println("Patient has been added failed");
            }
        }catch(SQLException e){
            e.printStackTrace();
        }
    }
//    take date from data_base and print in the console
    public void viewPatient() {
        String query = "SELECT * FROM patients";
        try{
            PreparedStatement preparedStatement = connection.prepareStatement(query);
//prepared statement take a query as argument and we not give any value so need to set value
            ResultSet resultSet = preparedStatement.executeQuery();
//       resultset print the value which came from table with formatting
            System.out.println("Patient details:");
            System.out.println("+-------------+--------------+------+--------+");
            System.out.println("| Patient Id  | Name         | Age  | Gender |");
            System.out.println("+-------------+--------------+------+--------+");
            while(resultSet.next()){
                int id=resultSet.getInt("id");
                String name=resultSet.getString("name");
                int age=resultSet.getInt("age");
                String gender=resultSet.getString("gender");
//                prinf help the formatting of output
                System.out.printf("|%-12s|%-14s|%-6s|%-8s|\n",id,name,age,gender);
                System.out.println("+-------------+--------------+------+--------+");
            }

        }catch(SQLException e){
            e.printStackTrace();
        }
    }
//    check patient exit in our database or not
    public boolean getPatientById(int id) {
        String query = "SELECT * FROM patients WHERE id=?";
        try{
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            if(resultSet.next()){
                return true;
            }else{
                return false;
            }
        }catch(SQLException e){
            e.printStackTrace();
        }
        return false;
    }
}
