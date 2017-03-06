package model;

import java.io.Serializable;

public class Employee implements Serializable {
	
	int empID;
	public String empName;
	int prefHours;
	public Schedule empAvailability;
	public Business business;
	
	public Employee(int id, String name, int hours, Schedule avail){
		this.empID = id;
		this.empName = name;
		this.prefHours=hours;
		this.empAvailability = avail;
		this.business = null;
	}

}
