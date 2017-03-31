package model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

import controller.Database;

public class Employee implements Comparable<Employee> {

	private int empID;
	private String fullName, fname, lname, email = "placeholder_email";
	// private int prefHours; we could maybe add preferred hours to iteration 3?
	// for now I added a public final maxEmpHours into the schedule class
	// to make sure employees aren't scheduled > legal/organization limits for a
	// week
	private boolean isManager;
	private Availability availability;

	public static String getTableName() {
		return "employee";
	}
	
	public static int getIDForLogin(String username, String password) {
		ArrayList<HashMap<String, String>> res = Database.executeSelectQuery(
				String.format("SELECT emp_id FROM login WHERE username='%s' AND password='%s'", username, password));
		if (res.isEmpty())
			return -1;
		return Integer.parseInt(res.get(0).get("emp_id"));
	}
	
	public static void loadFromID(int id) {
		String query = "SELECT * FROM " + getTableName() + " WHERE id=" + id;
		ArrayList<HashMap<String, String>> result = Database.executeSelectQuery(query);
		HashMap<String, String> hm = result.get(0);
		System.out.println("key:value");
		for (String key : hm.keySet()) {
			System.out.println(key + ":" + hm.get(key));
		}
		String availQuery = "SELECT * FROM " + Availability.getTableName() + " WHERE emp_id=" + id; 
		ArrayList<HashMap<String, String>> aresult = Database.executeSelectQuery(availQuery);
		HashMap<String, String> am = aresult.get(0);
		System.out.println("key:value");
		for (String key : am.keySet()) {
			System.out.println(key + ":" + am.get(key));
		}
		//String[] avail = new String[7];
		//Employee loaded = new Employee(Integer.parseInt(hm.get("id")), hm.get("fname"), hm.get("lname"), hm.get("email"), null, false);
	}

	public Employee(int id, String fname, String lname, String email, String[] avail, boolean isManager) {
		this.empID = id;
		this.fullName = fname + " " + lname;
		this.fname = fname;
		this.lname = lname;
		this.email = email;
		this.availability = new Availability(avail, isManager);
		this.isManager = isManager;
	}
	
	public Employee(int id) {
		this.empID = id;
	}

	public int getID() {
		return empID;
	}

	public String getFirstName() {
		return fname;
	}

	public String getLastName() {
		return lname;
	}

	public String getFullName() {
		return fullName;
	}

	public Availability getAvailability() {
		return availability;
	}

	public void setAvailability(Availability avail) {
		this.availability = avail;
	}

	/*
	 * returns true is this employee is a manager, false otherwise
	 */
	public boolean getIsManager() {
		return isManager;
	}

	@Override
	public int compareTo(Employee o) {
		return this.getID() - o.getID();
	}

	// delete Employee from DB
	public boolean delete() {
		this.getAvailability().delete(this.getID());
		return Database.executeManipulateDataQuery(
				String.format("DELETE FROM `%s`.`%s` WHERE `id`='%d'", Database.getName(), getTableName(), empID));
	}

	// save Employee into DB via insert or update
	public boolean save() {
		// UPDATE
		if (Database.tableContainsID(getTableName(), empID)) {
			return Database.executeManipulateDataQuery(String.format(
					"UPDATE `%s`.`%s` SET `fname`='%s',`lname`='%s',`email`='%s',`business_id`=%d" + " WHERE `id`=%d",
					Database.getName(), getTableName(), fname, lname, email, 0, empID));
		}

		// INSERT
		int newID = Database.getNextIDForTable(getTableName());
		if (newID < 0) {
			// an error occurred while getting next ID
			System.out.println("newid");
			return false;
		}

		// 0 is placeholder for business_id for now, since there is no ID in
		// business ATM
		return Database.executeManipulateDataQuery(String.format(
				"INSERT INTO `%s`.`%s` " + "(`id`, `fname`, `lname`, `email`, `business_id`)"
						+ " VALUES ('%d', '%s', '%s', '%s', %d)",
				Database.getName(), getTableName(), newID, fname, lname, email, 0));
	}

	
}
