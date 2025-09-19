package HospitalManagementSystem;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;
public class Doctors {
    private Connection connection;
    public Doctors(Connection connection) {
        this.connection = connection;
    }

    //    take date from data_base and print in the console
    public void viewDoctors() {
        String query = "SELECT * FROM doctors";
        try{
            PreparedStatement preparedStatement = connection.prepareStatement(query);
//prepared statement take a query as argument 'and' we not give any value so need to set value
            ResultSet resultSet = preparedStatement.executeQuery();
//       resultset print the value which came from table with formatting
            System.out.println("Doctors details:");
            System.out.println("+------------+-------------+----------------+");
            System.out.println("| Doctor Id  | Name        | Specialization |");
            System.out.println("+------------+-------------+----------------+");
            while(resultSet.next()){
                int id=resultSet.getInt("id");
                String name=resultSet.getString("name");
                String specialization=resultSet.getString("specialization");
//                prinf help the formatting of output
                System.out.printf("|%-12s|%-13s|%-16s|\n",id,name,specialization);
                System.out.println("+-------------+--------------+------+--------+");
            }

        }catch(SQLException e){
            e.printStackTrace();
        }
    }
    //    check patient exit in our database or not
    public boolean getDoctorById(int id) {
        String query = "SELECT * FROM doctors WHERE id=?";
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
