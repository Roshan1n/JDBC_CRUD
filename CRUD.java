package lab19;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class CRUD {
	private static Connection connection = null;
	private static Scanner sc = new Scanner(System.in);

	public static void main(String[] args) {

		CRUD db = new CRUD();

		try {
			Class.forName("com.mysql.jdbc.Driver");
			String databaseURL = "jdbc:mysql://localhost:3306/sage";
			String userName = "root";
			String password = "Roshan@2001";
			connection = DriverManager.getConnection(databaseURL, userName, password);

			while (true) {
				System.out.println("Enter 1 for inserting data: ");
				System.out.println("Enter 2 for delete data: ");
				System.out.println("Enter 3 for select all data: ");
				System.out.println("Enter 4 for select with roll number data: ");
				System.out.println("Enter 5 for update data: ");
				System.out.println("Enter 6 for Exit:");
				System.out.print("Enter your choice to perform your task: ");

				int choice = Integer.parseInt(sc.next());
				switch (choice) {
				case 1:
					db.insertData();
					break;

				case 2:
					db.deleteData();
					break;

				case 3:
					db.selectallData();
					break;

				case 4:
					db.findByRoll();
					break;

				case 5:
					db.updateData();
					break;

				case 6:
					db.exitDB();
					return;

				default:
					System.out.println("Invalid choice. Please enter a number between 1 and 6.");
					break;
				}

			}
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

//	1 for inserting data:
	public void insertData() {

		String query = "INSERT INTO student (name, percentage, address) VALUES (?, ?, ?)";
		PreparedStatement prep = null;
		try {
			prep = connection.prepareStatement(query);
			System.out.print("Enter the name: ");
			prep.setString(1, sc.nextLine());

			System.out.print("Enter the percentage: ");
			prep.setDouble(2, sc.nextDouble());

			System.out.print("Enter the address: ");
			prep.setString(3, sc.next());

			int row = prep.executeUpdate();
			if (row > 0) {
				System.out.println("Data inserted successfully.");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

//	2 for delete data:
	private void deleteData() {
		System.out.print("Enter the roll_number u want to delete: ");
		int roll = sc.nextInt();
		String query = "delete from student where roll_number = " + roll;
		PreparedStatement prep = null;

		try {
			prep = connection.prepareStatement(query);
			prep.execute();
			System.out.println("Data deleted successfully");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

//	3 for select all data:
	private void selectallData() {

		System.out.println("All Student data!");
		String quary = "select * from student";
		PreparedStatement prep = null;
		ResultSet data = null;

		try {
			prep = connection.prepareStatement(quary);
			data = prep.executeQuery();
			System.out.println("roll number \t name \t percentage \t address");
			while (data.next()) {
				int rollNumber = data.getInt("roll_number");
				String naam = data.getString("name");
				Double percent = data.getDouble("percentage");
				String addre = data.getString("address");
				System.out.println(rollNumber + "\t" + naam + "\t" + percent + "\t" + addre);

			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

//	4 for select with roll number data:
	private void findByRoll() {
		// TODO Auto-generated method stub

		System.out.print("Enter the roll number u want to find: ");
		int roll = sc.nextInt();
		String query = "select * from student where roll_number = " + roll;
		PreparedStatement prep = null;
		ResultSet data = null;
		try {
			prep = connection.prepareStatement(query);
			data = prep.executeQuery();
			System.out.println(" name \t percentage \t address");
			while (data.next()) {
				String naam = data.getString("name");
				Double percent = data.getDouble("percentage");
				String addre = data.getString("address");
				System.out.println(naam + "\t\t" + percent + "\t\t" + addre);

			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

//5 for updateing data	
	private void updateData() {
		// TODO Auto-generated method stub
		System.out.print("Enter the roll number of the student you want to update: ");
		int roll = sc.nextInt();
		String query = "SELECT * FROM student WHERE roll_number = ?";
		PreparedStatement checkStatement;
		try {
			checkStatement = connection.prepareStatement(query);

			checkStatement.setInt(1, roll);
			ResultSet resultSet = checkStatement.executeQuery();
			if (!resultSet.next()) {
				System.out.println("Student with roll number " + roll + " does not exist.");
				return;
			}

			System.out.print("Enter the updated name: ");
			String updatedName = sc.next();

			System.out.print("Enter the updated percentage: ");
			double updatedPercentage = Double.parseDouble(sc.next());

			System.out.print("Enter the updated address: ");
			String updatedAddress = sc.next();

			String updateQuery = "UPDATE student SET name = ?, percentage = ?, address = ? WHERE roll_number = ?";
			PreparedStatement updateStatement = connection.prepareStatement(updateQuery);
			updateStatement.setString(1, updatedName);
			updateStatement.setDouble(2, updatedPercentage);
			updateStatement.setString(3, updatedAddress);
			updateStatement.setInt(4, roll);

			int rowsUpdated = updateStatement.executeUpdate();
			if (rowsUpdated > 0) {
				System.out.println("Data updated successfully.");
			} else {
				System.out.println("Failed to update data.");
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

//6 for exit program
	private void exitDB() {

		try {
			if (connection != null && !connection.isClosed()) {
				connection.close();
				System.out.println("Database connection closed.");
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
