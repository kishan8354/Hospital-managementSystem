package HospitalManagementSystem;
import java.sql.*;
import java.util.Scanner;
public class HospitalManagementSystem {
    private static final String url="jdbc:mysql://localhost:3306/hospital";
    private static final String username="root";
    private static final String password="9198";
//    above data is very 'improtant' so we make it private and static no one able to access it and modify it;
    public static void main(String[] args) {
        try{
//            load all driver which are necessary to connect with database
          Class.forName("com.mysql.cj.jdbc.Driver");
        }catch(ClassNotFoundException e){
            e.printStackTrace();
        }
        Scanner scanner = new Scanner(System.in);
        try{
//            for connection establishing with database
            Connection connection= DriverManager.getConnection(url,username,password);
            Patient patient=new Patient(connection,scanner);
            Doctors doctors=new Doctors(connection);
            while(true){
                System.out.println("Hospital Management System");
                System.out.println("1. Add Patient");
                System.out.println("2. View Patient");
                System.out.println("3. View Doctor");
                System.out.println("4. Book Appointment");
                System.out.println("5. Exit");
                System.out.print("Enter your choice: ");
                int choice=scanner.nextInt();
                switch (choice){
                    case 1:
                        patient.addPatient();
                        System.out.println("Patient has been successfully added");
                        break;
                    case 2:
                        patient.viewPatient();
                        System.out.println();
                        break;
                    case 3:
                        doctors.viewDoctors();
                        System.out.println();
                        break;
                    case 4:
                        BookAppointment(patient,doctors,connection,scanner);
                        System.out.println();
                    case 5:
                        return;
                    default:
                        System.out.println("Invalid choice");
                }
            }
        }catch(SQLException e){
            e.printStackTrace();
        }
    }
    public static void BookAppointment(Patient patient,Doctors doctor, Connection connection,Scanner scanner){
        System.out.println("Enter Patient ID");
        int patientID=scanner.nextInt();
        System.out.println("Enter Doctor ID");
        int doctorID=scanner.nextInt();
        System.out.println("Enter appointment date (yyyy-mm-dd):");
        String appointmentDate=scanner.next();
        if(patient.getPatientById(patientID) && doctor.getDoctorById(doctorID)){
            if(checkDoctorAvailability(doctorID,appointmentDate,connection)){
                String appointmentQuery="INSERT INTO appointments(patient_id,docter_id,appointment_date) VALUES (?,?,?)";
                try{
                    PreparedStatement preparedStatement = connection.prepareStatement(appointmentQuery);
                    preparedStatement.setInt(1,patientID);
                    preparedStatement.setInt(2,doctorID);
                    preparedStatement.setString(3,appointmentDate);
                    int affectedRows = preparedStatement.executeUpdate();
                    if(affectedRows>0){
                        System.out.println("Appointment has been added successfully");
                    }
                    else{
                        System.out.println("Appointment has not been added successfully");
                    }
                }catch(SQLException e){
                    e.printStackTrace();
                }
            }
            else{
               System.out.println("Doctor is not available on this Date check other date");
            }
        }else{
            System.out.println("Either doctor or patient does not exist");
        }
    }
    public static boolean checkDoctorAvailability(int doctorID,String appointmentDate,Connection connection){
        String query ="SELECT COUNT(*) FROM appointments WHERE docter_id=? AND appointment_date=?";
//        check number of count of row at given date if doctor available count==0 if count>0 doctor not available
        try{
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1,doctorID);
            preparedStatement.setString(2,appointmentDate);
            ResultSet resultSet = preparedStatement.executeQuery();
//            we execute the query with help of resultset
            if(resultSet.next()){
                int count=resultSet.getInt(1);
                if(count==0){
                    return true;
                }
                else return false;
            }
        }catch(SQLException e){
            e.printStackTrace();
        }
        return false;
    }
}
