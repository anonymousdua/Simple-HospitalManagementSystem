package HospitalManagementSystem;

import javax.print.Doc;
import java.sql.*;
import java.util.Scanner;

public class HospitalManagementSystem {

    private static final String url = "jdbc:mysql://localhost:3306/hospital";
    private static final String user = "root";
    private static final String password = "Hesoyam12&";

    public static void main(String[] args) {

        try{
            Class.forName("com.mysql.cj.jdbc.Driver");
            //Class.forName("com.mysql.cj.jdbc.Driver");
        }
        catch(ClassNotFoundException e){
            e.printStackTrace();
        }

        Scanner sc = new Scanner(System.in);

        try {
            Connection connection = DriverManager.getConnection(url, user, password);
            Patient patient = new Patient(connection, sc);
            Doctor doctor = new Doctor(connection);

            while (true){
                System.out.println("Hospital Management System");
                System.out.println("1. Add Patient");
                System.out.println("2. View Patient");
                System.out.println("3. Book Appointment");
                System.out.println("4. View Appointment");
                System.out.println("5. Exit");
                System.out.println("Enter your choice: ");

                int choice = sc.nextInt();

                switch(choice){
                    case 1:
                        patient.addPatient();
                        break;
                        //
                    case 2:
                        patient.viewPatient();
                        break;
                        // view patient
                    case 3:
                        doctor.viewDoctors();
                        break;
                        //
                    case 4:
                        bookAppointment(patient, doctor, connection, sc);
                        break;
                        //
                    case 5:
                        return;
                    default:
                        System.out.println("Enter valid choice: ");
                        break;
                }
            }

        }
        catch(SQLException e){
            e.printStackTrace();
        }
    }

    public static void bookAppointment(Patient patient, Doctor doctor, Connection connection, Scanner sc){

        System.out.println("Enter patient id: ");
        int patientId = sc.nextInt();
        System.out.println("enter doctor id: ");
        int doctorId = sc.nextInt();
        System.out.println("Enter appointment date (YYYY-MM-DD): ");
        String appointmentDate = sc.next();

        if(patient.getPatientById(patientId) && doctor.getDoctorById(doctorId)){

            if(checkDoctorAvailability(doctorId, appointmentDate, connection)){

                String appointmentQuery = "INSERT INTO appointments (patient_id, doctor_id, appointment_date) values(?, ?, ?);";

                try {
                    PreparedStatement preparedStatement = connection.prepareStatement(appointmentQuery);
                    preparedStatement.setInt(1, patientId);
                    preparedStatement.setInt(2, doctorId);
                    preparedStatement.setString(3, appointmentDate);

                    int affectedRows = preparedStatement.executeUpdate();

                    if(affectedRows > 0){
                        System.out.println("Appointment Booked Successfully");
                    }
                    else {
                        System.out.println("Appointment Booking Failed");
                    }
                }
                catch (SQLException e){
                    e.printStackTrace();
                }
            }
            else {
                System.out.println("Doctor is not available on this date");
            }
        }
        else {
            System.out.println("Either doctor or patient does not exist");
        }
    }

    public static boolean checkDoctorAvailability(int doctorId, String appointmentDate, Connection connection){
        String query = "SELECT count(*) FROM appointments WHERE doctor_id=? AND appointment_date=?";

        try{
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, doctorId);
            preparedStatement.setString(2, appointmentDate);

            ResultSet resultSet = preparedStatement.executeQuery();

            if(resultSet.next()){

                int count = resultSet.getInt(1);

                if(count == 0){
                    return true;
                }
                else {
                    return false;
                }
            }
        }
        catch (SQLException e){
            e.printStackTrace();
        }

        return false;
    }
}
