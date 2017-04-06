package model;

import java.util.ArrayList;
import java.util.HashMap;

import controller.Database;

public class Employee implements Comparable<Employee> {
	
	private static Employee loggedIn;
	private int empID;
	private String fullName, fname, lname, email = "placeholder_email";
	private boolean isManager;
	private Availability availability;
	// the business this Employee belongs to
	private Business bus;
	private String currentSchedule;

	public static String getTableName() {
		return "employee";
	}

	public static int getIDForLogin(String username, String password) {
		System.out.printf("Employee.getIdForLogin(): getting ID for username '%s'\n", username);
		ArrayList<HashMap<String, String>> res = Database.executeSelectQuery(
				String.format("SELECT emp_id FROM login WHERE username='%s' AND password='%s'", username, password));
		if (res.isEmpty())
			return -1;
		return Integer.parseInt(res.get(0).get("emp_id"));
	}
	
	/**
	 * If you are creating a brand new employee, use -1 for id. You also need to set the business.
	 */
	public Employee(int id, String fname, String lname, String email, String[] avail, boolean isManager) {
		if (id == -1) {
			id = Database.getNextIDForTable(getTableName()); 
			if (id < 0) { 
				System.out.println("an error occurred while getting next ID return false");
			}
		}
		this.empID = id;
		this.fullName = fname + " " + lname;
		this.fname = fname;
		this.lname = lname;
		this.email = email;
		this.availability = new Availability(avail, isManager);
		this.isManager = isManager;
	}
	
	/**
	 * Returns null if the employee does not exist in the database, otherwise,
	 * returns the Employee object with filled data fields.
	 */
	public static Employee loadFromID(int id, Business business) {
		System.out.println("Employee.loadFromID(): loading from id " + id);
		String query = "SELECT * FROM " + getTableName() + " WHERE id=" + id;
		ArrayList<HashMap<String, String>> result = Database.executeSelectQuery(query);
		if (result.size() == 0)
			return null;
		HashMap<String, String> hm = result.get(0);
		System.out.println("Employee.loadFromID(): loading availability");
		String[] avail = Availability.loadAvailabilityFromID(id);
		boolean isManager;
		if (hm.get("is_manager").equals("1"))
			isManager = true;
		else
			isManager = false;
		Employee loaded = new Employee(id, hm.get("fname"), hm.get("lname"), hm.get("email"), avail, isManager);
		System.out.println("Employee.loadFromID(): loading business");
		if (business == null) {
			Business bus = Business.loadFromID(Integer.parseInt(hm.get("business_id")));
			bus.addEmployee(loaded);
			loaded.setBusiness(bus);
		} else {
			loaded.setBusiness(business);
		}
		setCurrentSchedule(loaded);
		return loaded;
	}

	/**
	 * Set's this Employee's Business to bus.
	 */
	public void setBusiness(Business bus) {
		bus.addEmployee(this);
		this.bus = bus;
	}
	
	/**
	 * Returns this Employee's Business.
	 */
	public Business getBusiness() {
		return this.bus;
	}
	
	/**
	 * Sets this.currentSchedule to the appropriate
	 * String representation of this Employee's scheduled shifts in its Business's 
	 * master schedule. 
	 */
	public static void setCurrentSchedule(Employee emp) {
		System.out.println("Employee.setCurrentSchedule(): setting schedule from employee " + emp.getID());
		ArrayList<ArrayList<TimeSlot>> shiftList = emp.getBusiness().getEmployeesCurrentSchedule(emp.getID());
		String schedule = "";
		for (int i = 0; i < 7; i++) {
			if (shiftList.get(i).size() != 0) {
				for (TimeSlot ts : shiftList.get(i)) {
					schedule += ts.toString();
					schedule += "\n";
				}
			}
		}
		schedule.trim();
		emp.setSched(schedule);
	}
	
	/**
	 * Sets this Employee's schedule to sched.
	 */
	public void setSched(String sched) {
		this.currentSchedule = sched;
	}
	
	/**
	 * Returns the String representation of this Employee's current
	 * scheduled shifts.
	 */
	public String getSchedule() {
		return this.currentSchedule;
	}

	/**
	 * Returns this Employee's id.
	 */
	public int getID() {
		return empID;
	}

	/**
	 * Returns this Employee's first name.
	 */
	public String getFirstName() {
		return fname;
	}

	/**
	 * Returns this Employee's last name.
	 */
	public String getLastName() {
		return lname;
	}

	/**
	 * Returns this Employee's full name.
	 */
	public String getFullName() {
		return fullName;
	}

	/**
	 * Returns this Employee's Availability object.
	 */
	public Availability getAvailability() {
		return availability;
	}

	/**
	 * Sets this Employee's Availability object to avail.
	 */
	public void setAvailability(Availability avail) {
		this.availability = avail;
	}

	/**
	 * returns true is this employee is a manager, false otherwise
	 */
	public boolean getIsManager() {
		return isManager;
	}

	@Override
	public int compareTo(Employee o) {
		return this.getID() - o.getID();
	}

	/**
	 * Deletes this Employee from the Database.
	 */
	public boolean delete() {
		this.getAvailability().delete(this.getID());
		return Database.executeManipulateDataQuery(
				String.format("DELETE FROM `%s`.`%s` WHERE `id`='%d'", Database.getName(), getTableName(), empID));
	}

	/**
	 * Saves this Employee (by Updating of Inserting it) in the Database.
	 */
	public boolean save() {
		
		boolean result;
		// UPDATE
		if (Database.tableContainsID(getTableName(), empID)) {
			result = Database.executeManipulateDataQuery(String.format(
					"UPDATE `%s`.`%s` SET `fname`='%s',`lname`='%s',`email`='%s',`business_id`=%d,`is_manager`=%d"
							+ " WHERE `id`=%d",
					Database.getName(), getTableName(), fname, lname, email, 0, empID, isManager ? 1 : 0));
		} else {

		result = Database.executeManipulateDataQuery(String.format(
				"INSERT INTO `%s`.`%s` " + "(`id`, `fname`, `lname`, `email`, `business_id`, `is_manager`)"
						+ " VALUES ('%d', '%s', '%s', '%s', %d, %d)",
				Database.getName(), getTableName(), empID, fname, lname, email, bus.getID(), isManager ? 1 : 0));
		}
		this.getAvailability().save(empID);
		return result;
	}

	public static Employee getLoggedIn() {
		return loggedIn;
	}

	public static void setLoggedIn(Employee loggingIn) {
		loggedIn = loggingIn;
	}

}
