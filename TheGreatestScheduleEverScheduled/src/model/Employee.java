package model;

import java.io.Serializable;

public class Employee implements Serializable {
	
	int empID;
	String empName;
	int prefHours;
	Schedule empAvailability;
	
	public Employee(int id, String name, int hours, Schedule avail){
		this.empID = id;
		this.empName = name;
		this.prefHours=hours;
		this.empAvailability = avail;
	}

}
