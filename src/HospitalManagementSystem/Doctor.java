package HospitalManagementSystem;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class Doctor {

    private Connection connection;


    public Doctor(Connection connection) {
        this.connection = connection;
    }



    public void viewDoctors(){

        String query = "SELECT * FROM doctors";

        try {

            PreparedStatement preparedStatement = connection.prepareStatement(query);
            ResultSet resultSet = preparedStatement.executeQuery();
            System.out.println("Doctor List: ");
            System.out.println("+--------------------+--------------------+--------------------+");
            System.out.println("| Doctor id          | Name               | Specialization     |");
            System.out.println("+--------------------+--------------------+--------------------+");

            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                int age = resultSet.getInt("age");
                String specialization = resultSet.getString("specializtion");
                System.out.printf("| %-18s | %-18s |%-18s |\n", id, name, specialization);
                System.out.println("+--------------------+--------------------+--------------------+");
            }
        }
        catch (SQLException e){
            e.printStackTrace();
        }
    }

    public boolean getDoctorById(int id){
        String query = "SELECT * FROM doctors WHERE id = ?";

        try{

            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();

            if(resultSet.next()) {
                return true;
            }
            else {
                return false;
            }

        }
        catch (SQLException e){
            e.printStackTrace();
        }

        return false;
    }

}
