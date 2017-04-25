package controller;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;

import model.Availability;
import model.Business;
import model.Employee;
import model.Schedule;

/* the database is named "ccdb" which stands for cool cucumber database
    
 Example query: SELECT username FROM ccdb.login WHERE emp_id=1
 */
public class Database {

	public static String dbName = "ccdb";
	public static Connection conn = null;
	private static boolean persistConnection = false;

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
		if (data.isEmpty() || data.get(0).get("MAX(id)") == null) {
			return 0; // there were no other records in the table
		}
		// get the max id from first (and only) record returned
		// then add 1 to make new id
		return Integer.parseInt(data.get(0).get("MAX(id)")) + 1;
	}

	// returns if/if not a successful execution
	// use this for UPDATE/INSERT/DELETE
	public static boolean executeManipulateDataQuery(String query) {
		startConnection();
		boolean success = true;
		try {
			Statement statement = conn.createStatement();
			statement.execute(query); 
			if (!persistConnection) conn.close();
		} catch (SQLException e) {
			success = false;
			System.err.println("SQLException with query: " + query);
			e.printStackTrace();
		}
		return success;
	}

	// Use only for SELECTs
	// Converts ResultSet to a consistently accessible ArrayList<String[]>
	public static ArrayList<HashMap<String, String>> executeSelectQuery(String query) {
		startConnection();
		try {
			Statement statement = conn.createStatement();
			ResultSet rs = statement.executeQuery(query);
			ResultSetMetaData metadata = rs.getMetaData();
			ArrayList<HashMap<String, String>> data = new ArrayList<>();

			// save all the records into the ArrayList
			// each record is a mapping from attribute name => value
			while (rs.next()) {
				HashMap<String, String> record = new HashMap<>();
				for (int i = 1; i <= metadata.getColumnCount(); i++) {
					record.put(metadata.getColumnName(i), rs.getString(i));
				}
				data.add(record);
			}
			if (!persistConnection) conn.close();
			return data;
		} catch (SQLException e) {
			System.err.println("SQLException with query: " + query);
			e.printStackTrace();
			return null; // oh no!
		}
	}

	private static void startConnection() {
		try {
			if (conn != null && !conn.isClosed()) {
				return; // already connected
			}
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection(
					"jdbc:mysql://ccdbinstance.cu5co55mqa04.us-west-2.rds.amazonaws.com:3306/ccdb", "username",
					"password");
		} catch (Exception e) {
			System.err.println("Connection with DB could not be established");
			e.printStackTrace();
		}
	}
	
	public static void beginPersistentConnection() {
		startConnection();
		persistConnection = true;
	}

	public static void endPersistentConnection() {
		stopConnection();
		persistConnection = false;
	}
	
	public static void stopConnection() {
		try {
			if (conn != null && !conn.isClosed()) {
				conn.close();
			}
		} catch (SQLException e) {
			System.err.println("Could not close connection to Database");
			e.printStackTrace();
		}
	}

	// check if a certain table already contains an id (check for if you want to
	// UPDATE or INSERT)
	public static boolean tableContainsID(String tableName, int id) {
		ArrayList<HashMap<String, String>> res;
		if (tableName.equals(Availability.getTableName())) {
			res = executeSelectQuery(
					String.format("SELECT * FROM `%s`.`%s` WHERE emp_id = %d", Database.getName(), tableName, id));
		} else {
			res = executeSelectQuery(
					String.format("SELECT * FROM `%s`.`%s` WHERE id = %d", Database.getName(), tableName, id));
		}
		return res != null && !res.isEmpty();
	}

	public static boolean masterSchedContainsIDPair(int schedID, int timeSlotID) {
		ArrayList<HashMap<String, String>> res = executeSelectQuery(
				String.format("SELECT * FROM `%s`.`%s` WHERE sched_id = %d AND timeslot_id = %d", Database.getName(),
						Schedule.getMasterScheduleTableName(), schedID, timeSlotID));
		return res != null && !res.isEmpty();
	}
}
