package model;

public class Employee {
	
	private int empID;
	private String fullName, fname, lname;
	//private int prefHours; we could maybe add preferred hours to iteration 3?
	   // for now I added a public final maxEmpHours into the schedule class
	   // to make sure employees aren't scheduled > legal/organization limits for a week
	private boolean isManager;
	private Availability availability;
	
	public static String getTableName() {
		return "employee";
	}

	public Employee(int id, String fname, String lname, String[] avail, boolean isManager){
		this.empID = id;
		this.fullName = fname + " " + lname;
		this.fname = fname;
		this.lname = lname;
		this.availability = new Availability(avail, isManager);
		this.isManager = isManager;
		// TODO add new employee information into the appropriate database tables
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
	
}
