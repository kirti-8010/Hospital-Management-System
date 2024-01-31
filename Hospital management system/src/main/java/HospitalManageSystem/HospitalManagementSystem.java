package HospitalManageSystem;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class HospitalManagementSystem {   //  Driver class
	private static final String url = "jdbc:mysql://localhost:3306/hospital";  // private is declared for security purpose
	
	private static final String username = "root";
	private static final String password = "admin";

	private static String appointmentDate;
	
	public static void main(String[] args)
	{
		try
		{
			Class.forName("com.mysql.cj.jdbc.Driver");
		}
		catch(ClassNotFoundException e)
		{
			e.printStackTrace();
		}
		
		Scanner scanner = new Scanner(System.in);
		try 
		{
			Connection connection = DriverManager.getConnection(url,username, password);
			Patient patient = new Patient(connection,scanner);
			Doctor doctor = new Doctor(connection);
			
			while(true)
			{
				System.out.println("HOSPITAL MANAGE SYSTEM");
				System.out.println("1. Add Patient");
				System.out.println("2. View Patient");
				System.out.println("3. View Doctors");
				System.out.println("4. Book Appointment");
				System.out.println("5. Exit");
				
				System.out.println("Enter your choice");
				int choice = scanner.nextInt();
				
				switch(choice)
				{
					case 1:
						// Add Patient
						patient.addPatient();
						System.out.println();
						break;
					
					case 2:
						//View Patient
						patient.viewPatients();
						System.out.println();
						break;
						
					case 3:
						// View Doctors
						doctor.viewDoctors();
						System.out.println();
						break;
						
					case 4:
						// Book Appointment
						bookAppointment(patient, doctor, connection, scanner);
						System.out.println();
						break;
					case 5:
						return;
					
					default:
						System.out.println("Enter the Valid Choice");
						break;
				}
			}
		}
		catch(SQLException e)
		{
			e.printStackTrace();
		}
	}
	
//	book appointment
	public static void bookAppointment(Patient patient, Doctor doctor,Connection connection, Scanner scanner)
	{
		System.out.print("Enter Patient_Id: ");
		int patientId = scanner.nextInt();
		
		System.out.print("Enter Doctor_Id: ");
		int doctorId = scanner.nextInt();
		
		System.out.print("Enter appointment date (yyyy-mm-dd): ");
		String appointment = scanner.next();
		
		if(patient.getPatientById(patientId) && doctor.getDoctorById(doctorId))
		{
			if(checkDoctorAvailiability(doctorId, appointmentDate,connection))
			{
				String appointmentQuery = "insert into appointment(patient_id, doctor_id, appointment_date)values(?,?,?)";
				try {
					PreparedStatement preparedStatement = connection.prepareStatement(appointmentQuery);
					preparedStatement.setInt(1,patientId);
					preparedStatement.setInt(2,doctorId);
					preparedStatement.setString(3,appointmentDate);
					int rowsAffected = preparedStatement.executeUpdate();
					if(rowsAffected > 0)
					{
						System.out.println("Appointment Booked");
					}
					else
					{
						System.out.println("Failed Appointment");
					}
				}
				catch(SQLException e)
				{
					e.printStackTrace();
				}
			}
			else
			{
				System.out.println("Doctor is not available on this date");
			}
		}
		else
		{
			System.out.println("Either doctor or patient doesn't exist");
		}
	}
	private static boolean checkDoctorAvailiability(int doctorId, Object appointmentDate, Connection connection) {
	// TODO Auto-generated method stub
		String query = "select count(*) from appointment where doctor_id = ? and appointment_date = ?";
		try
		{
			PreparedStatement preparedStatement = connection.prepareStatement(query);
			preparedStatement.setInt(1, doctorId);
			preparedStatement.setString (2, (String) appointmentDate);
			ResultSet resultSet = preparedStatement.executeQuery();
			if(resultSet.next())
			{
				int count = resultSet.getInt(1);
				if(count==0)
				{
					return true;
				}
				else
				{
					return false;
				}
			}
		}
		catch(SQLException e)
		{
			e.printStackTrace();
		}
	return false;
}
}
