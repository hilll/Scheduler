package model;

import java.io.Serializable;

import controller.Database;

public class Employee implements Serializable {

	int empID;
	public String email = "placeholder@email.com", empName, fname, lname;
	int prefHours;
	public Schedule empAvailability;
	public Business business;

	public static String getTableName() {
		return "employee";
	}

	// for testing
	public Employee() {

	}

	public Employee(int id, String name, int hours, Schedule avail) {
		this.empID = id;
		this.empName = name;
		this.prefHours = hours;
		this.empAvailability = avail;
		this.business = null;
	}

	public Employee(int id, String fname, String lname, String email, int hours, Schedule avail, Business b) {
		this.empID = id;
		this.empName = fname + " " + lname;
		this.fname = fname;
		this.lname = lname;
		this.email = email;
		this.prefHours = hours;
		this.empAvailability = avail;
		this.business = b;
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
		return empName;
	}

	// delete Employee from DB
	public boolean delete() {
		return Database.executeManipulateDataQuery(
				String.format("DELETE FROM `%s`.`%s` WHERE `id`='%d'", Database.getName(), getTableName(), empID));
	}

	// save Employee into DB via insert or update
	public boolean save() {
		// UPDATE
		if (Database.tableContainsID(getTableName(), empID)) {
			return Database.executeManipulateDataQuery(String.format(
					"UPDATE `%s`.`%s` SET `fname`='%s',`lname`='%s',`email`='%s',`business_id`=%d"
							+ " WHERE `id`=%d",
					Database.getName(), getTableName(), fname, lname, email, 0, empID));
		}

		// INSERT
		int newID = Database.getNextIDForTable(getTableName());
		if (newID < 0) {
			// an error occurred while getting next ID
			return false;
		}

		// 0 is placeholder for business_id for now, since there is no ID in business ATM
		return Database.executeManipulateDataQuery(String.format(
				"INSERT INTO `%s`.`%s` " + "(`id`, `fname`, `lname`, `email`, `business_id`)"
						+ " VALUES ('%d', '%s', '%s', '%s', %d)",
				Database.getName(), getTableName(), newID, fname, lname, email, 0));
	}
}
