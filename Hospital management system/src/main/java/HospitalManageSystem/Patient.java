package HospitalManageSystem;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class Patient {
	private Connection connection;
	private Scanner scanner;

	public Patient(Connection connection, Scanner scanner)
	{
		this.connection = connection;
		this.scanner = scanner;
	}
	public void addPatient()
	{
		System.out.print("Enter Patient's Name: ");
		String name = scanner.next(); 
		
		System.out.print("Enter Patient's Age: ");
		int age = scanner.nextInt();
		
		System.out.print("Enter Patient's Gender: ");
		String gender = scanner.next();
		
		try {
			String query = "INSERT INTO patients(name, age, gender) values(?,?,?)";
			PreparedStatement preparedStatement = connection.prepareStatement(query);
			preparedStatement.setString(1, name);
			preparedStatement.setInt(2, age);
			preparedStatement.setString(3, gender);
			
			int affectedRows = preparedStatement.executeUpdate();
			if(affectedRows > 0)
			{
				System.out.println("Patient's Data Insert Successfully");
			}
			else
			{
				System.out.println("Failed to Insert Patient's Data");
			}
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void viewPatients()
	{
		String query = "select * from patients";

		try
		{
			PreparedStatement preparedStatement = connection.prepareStatement(query);
			ResultSet resultSet = preparedStatement.executeQuery();
			
			System.out.println("Patients: ");
			System.out.println("*-------------*------------------*----------*---------*");
			System.out.println("| Patient_id  |      Name        |    Age   |  Gender |");
			System.out.println("*-------------*------------------*----------*---------*");
			
			while(resultSet.next())
			{
				int id = resultSet.getInt("id");
				String name = resultSet.getString("name");
				int age = resultSet.getInt("age");
				String gender = resultSet.getString("gender");
				
				System.out.printf("| %-11s | %-16s | %-8s | %-7s |\n",id,name,age,gender);
				System.out.println("*-------------*------------------*----------*---------*");
			}
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public boolean getPatientById(int id)
	{
		String query = "select * from patients where id = ?";
		try {
			PreparedStatement preparedStatement = connection.prepareStatement(query);
			preparedStatement.setInt(1, id);
			ResultSet resultSet = preparedStatement.executeQuery();
			
			if(resultSet.next())
			{
				return false;
			}
			else
			{
				return true;
			}
		}
		
		catch(SQLException e)
		{
			e.printStackTrace();
		}
		return false;
	}
}
