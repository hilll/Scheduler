package controller;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;

import model.Employee;
import model.Schedule;

/* the database is named "ccdb" which stands for cool cucumber database
    
 Example query: SELECT username FROM ccdb.login WHERE emp_id=1
 */
public class Database {

	public static String dbName = "ccdb";

	public static void main(String args[]) {
		
		// a little test to see how things work
		ArrayList<HashMap<String, String>> data = executeSelectQuery("SELECT * FROM " + Employee.getTableName());
		System.out.println("Data in employee table: ");
		printData(data);
		System.out.println();

		// to get the next ID to use for some table
		System.out.println("Next new employee will have ID of: " + getNextIDForTable(Employee.getTableName()));
		System.out.println();

		// adding a new employee to the DB
		Employee e = new Employee(getNextIDForTable(Employee.getTableName()), "Test", "Person", "email", null, false);
		System.out.println("Inserting new Employee: " + e.getFullName());
		e.save();
		System.out.println("Data in employee table: ");
		data = executeSelectQuery("SELECT * FROM " + Employee.getTableName());
		printData(data);
		System.out.println();

		// delete the employee, because we don't Test Person >:O!
		System.out.println("Deleting " + e.getFullName() + "...");
		e.delete();
		System.out.println("Data in employee table: ");
		data = executeSelectQuery("SELECT * FROM " + Employee.getTableName());
		printData(data);
		System.out.println();

		// see the availability format for an employee - I only added data for
		// myself (emp_id=1) for now
		System.out.println("Availability test:");
		data = executeSelectQuery("SELECT * FROM availability WHERE emp_id = 1");
		printData(data);
		
		// UPDATE test - id 0 already exists in DB
		System.out.println("Update test:");
		System.out.println("BEFORE:");
		data = executeSelectQuery("SELECT * FROM employee WHERE id = 0");
		printData(data);
		Employee updateEmp = new Employee(0, "poop", "White", "pooping", null, false);
		updateEmp.save();
		System.out.println("AFTER:");
		data = executeSelectQuery("SELECT * FROM employee WHERE id = 0");
		printData(data);
		Employee resetEmp = new Employee(0, "Sarina", "White", "sarinarw@email.arizona.edu", null, false);
		resetEmp.save();
	}

	// * can be used to print the returned DB data from executeSelectQuery()
	// * mainly for testing - making sure your SELECT query is getting the right
	// output
	public static void printData(ArrayList<HashMap<String, String>> data) {
		for (HashMap<String, String> record : data) {
			for (String key : record.keySet()) {
				System.out.println(key + ":" + record.get(key));
			}
			System.out.println(); // extra newline between records
		}
	}

	public static String getName() {
		return dbName;
	}

	// get the next id PK to use for a new insert into DB table
	public static int getNextIDForTable(String tableName) {
		ArrayList<HashMap<String, String>> data = executeSelectQuery(
				String.format("SELECT MAX(id) FROM %s", tableName));
		if (data == null) {
			return -1; // some error occurred during the query
		}
		if (data.isEmpty()) {
			return 0; // there were no other records in the table
		}
		// get the max id from first (and only) record returned
		// then add 1 to make new id
		return Integer.parseInt(data.get(0).get("MAX(id)")) + 1;
	}

	// returns if/if not a successful execution
	// use this for UPDATE/INSERT/DELETE
	public static boolean executeManipulateDataQuery(String query) {
		Connection con = getConnection();
		boolean success = false;
		try {
			Statement statement = con.createStatement();
			success = statement.execute(query);
			con.close();
		} catch (SQLException e) {
			System.err.println("SQLException with query: " + query);
			e.printStackTrace();
		}
		return success;
	}

	// Use only for SELECTs
	// Converts ResultSet to a consistently accessible ArrayList<String[]>
	public static ArrayList<HashMap<String, String>> executeSelectQuery(String query) {
		Connection con = getConnection();
		try {
			Statement statement = con.createStatement();
			ResultSet rs = statement.executeQuery(query);
			ResultSetMetaData metadata = rs.getMetaData();
			ArrayList<HashMap<String, String>> data = new ArrayList<>();

			// save all the records into the arraylist
			// each record is a mapping from attribute name => value
			while (rs.next()) {
				HashMap<String, String> record = new HashMap<>();
				for (int i = 1; i <= metadata.getColumnCount(); i++) {
					record.put(metadata.getColumnName(i), rs.getString(i));
				}
				data.add(record);
			}
			con.close();
			return data;
		} catch (SQLException e) {
			System.err.println("SQLException with query: " + query);
			e.printStackTrace();
			return null; // oh no!
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

	// check if a certain table already contains an id (check for if you want to UPDATE or INSERT)
	public static boolean tableContainsID(String tableName, int id) {
		ArrayList<HashMap<String, String>> res = executeSelectQuery(
				String.format("SELECT * FROM `%s`.`%s` WHERE id = %d", Database.getName(), tableName, id));
		if (res.isEmpty()) {
			return false;
		}
		return true;
	}
}
