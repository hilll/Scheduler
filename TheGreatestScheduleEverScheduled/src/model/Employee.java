package model;

import java.io.Serializable;

public class Employee implements Serializable {
	
	int empID;
	public String empName, fname, lname; //TODO: change empName to fullName ?
	int prefHours;
	public Schedule empAvailability;
	public Business business;
	
	public static String getTableName() {
		return "employee";
	}
	
	// for testing
	public Employee() {
		
	}
	
	public Employee(int id, String name, int hours, Schedule avail){
		this.empID = id;
		this.empName = name;
		this.prefHours=hours;
		this.empAvailability = avail;
		this.business = null;
	}

	public Employee(int id, String fname, String lname, int hours, Schedule avail, Business b){
		this.empID = id;
		this.empName = fname + " " + lname;
		this.fname = fname;
		this.lname = lname;
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
	
}
