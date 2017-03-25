package controller;

import java.sql.*;
import java.util.ArrayList;

import model.Employee;

/* the database is named "ccdb" which stands for cool cucumber database
 the tables that currently exist are:
 availability
	emp_id INT
    sunday, monday...saturday VARCHAR(96) but should always be 96chars
 employee
    id INT
    fname VARCHAR(45)
    lname VARCHAR(45)
 login (this has 1 record for emp_id=1)
    emp_id INT
    username VARCHAR(45)
    password VARCHAR(45)
    
 Example query: SELECT username FROM ccdb.login WHERE emp_id=1
 */
public class Database {

	public static String dbName = "ccdb";
	
	public static void main(String args[]) {
		// a little test to see how things work
		ArrayList<String[]> data = executeSelectQuery("SELECT * FROM " + Employee.getTableName());
		System.out.println("Data in employee table: ");
		printData(data);
		System.out.println();

		// to get the next ID to use for some table
		System.out.println("Next new employee will have ID of: " + getNextIDForTable(Employee.getTableName()));
		System.out.println();

		// adding a new employee to the DB
		Employee e = new Employee(getNextIDForTable(Employee.getTableName()), "Test", "Person", 0, null, null);
		System.out.println("Inserting new Employee: " + e.getFullName());
		insert(e);
		System.out.println("Data in employee table: ");
		data = executeSelectQuery("SELECT * FROM " + Employee.getTableName());
		printData(data);
		System.out.println();

		// delete the employee, because we don't Test Person >:O!
		System.out.println("Deleting " + e.getFullName() + "...");
		delete(e);
		System.out.println("Data in employee table: ");
		data = executeSelectQuery("SELECT * FROM " + Employee.getTableName());
		printData(data);
		System.out.println();

		// see the availability format for an employee - I only added data for
		// myself (emp_id=1) for now
		System.out.println("Availability test:");
		data = executeSelectQuery("SELECT * FROM availability WHERE emp_id = 1");
		printAvailability(data);
	}

	// * can be used to print the returned DB data from executeSelectQuery()
	// * mainly for testing - making sure your SELECT query is getting the right
	// output
	public static void printData(ArrayList<String[]> data) {
		for (String[] record : data) {
			for (String attribute : record) {
				System.out.print(attribute + " ");
			}
			System.out.println();
		}
	}

	// more specific for availability because of the very long 01010101 strings
	// and it looks super horrible (unreadable) as a single line
	public static void printAvailability(ArrayList<String[]> data) {
		for (String[] record : data) {
			System.out.println("emp_id: " + record[0]);
			System.out.println("sunday:    " + record[1]);
			System.out.println("monday:    " + record[2]);
			System.out.println("tuesday:   " + record[3]);
			System.out.println("wednesday: " + record[4]);
			System.out.println("thursday:  " + record[5]);
			System.out.println("friday:    " + record[6]);
			System.out.println("sunday:    " + record[7]);
			System.out.println("each availability string has length: " + record[1].length());
		}
	}

	// inserting an employee
	public static boolean insert(Employee e) {
		int newID = getNextIDForTable(Employee.getTableName());
		if (newID < 0) {
			// an error occurred while getting next ID
			return false;
		}
		return executeManipulateDataQuery(
				String.format("INSERT INTO `%s`.`%s`" + "(`id`, `fname`, `lname`) VALUES ('%d', '%s', '%s')", dbName,
						Employee.getTableName(), newID, e.getFirstName(), e.getLastName()));
	}

	public static boolean delete(Employee e) {
		return executeManipulateDataQuery(
				String.format("DELETE FROM `%s`.`%s` WHERE `id`='%d'", dbName, Employee.getTableName(), e.getID()));
	}

	private static int getNextIDForTable(String tableName) {
		ArrayList<String[]> data = executeSelectQuery(String.format("SELECT MAX(id) FROM %s", tableName));
		if (data == null) {
			return -1; // some error occurred during the query
		}
		if (data.isEmpty()) {
			return 0; // there were no other records in the table
		}
		// get the first value of the first record returned
		// (which is the MAX id currently in the table)
		// and add 1 to get next new ID
		return Integer.parseInt(data.get(0)[0]) + 1;
	}

	// returns if/if not a successful execution
	// use this for UPDATE/INSERT/DELETE
	private static boolean executeManipulateDataQuery(String query) {
		Connection con = getConnection();
		boolean success = false;
		try {
			Statement statement = con.createStatement();
			success = statement.execute(query);
			con.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return success;
	}

	// Use only for SELECTs
	// Converts ResultSet to a consistently accessible ArrayList<String[]>
	private static ArrayList<String[]> executeSelectQuery(String query) {
		Connection con = getConnection();
		try {
			Statement statement = con.createStatement();
			ResultSet rs = statement.executeQuery(query);
			ResultSetMetaData metadata = rs.getMetaData();
			ArrayList<String[]> data = new ArrayList<>();
			// save all the records into the arraylist
			while (rs.next()) {
				String[] record = new String[metadata.getColumnCount()];
				for (int i = 1; i <= metadata.getColumnCount(); i++) {
					record[i - 1] = rs.getString(i);
				}
				data.add(record);
			}
			con.close();
			return data;
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}

	private static Connection getConnection() {
		Connection con = null;
		try {
			con = DriverManager.getConnection(
					"jdbc:mysql://ccdbinstance.cu5co55mqa04.us-west-2.rds.amazonaws.com:3306/ccdb", "username",
					"password");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return con;
	}
}
