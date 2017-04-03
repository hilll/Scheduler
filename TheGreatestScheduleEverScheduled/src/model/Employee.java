package model;

import java.util.ArrayList;
import java.util.HashMap;

import controller.Database;

public class Employee implements Comparable<Employee> {
	private static Employee loggedIn;

	private int empID;
	private String fullName, fname, lname, email = "placeholder_email";
	// private int prefHours; we could maybe add preferred hours to iteration 3?
	// for now I added a public final maxEmpHours into the schedule class
	// to make sure employees aren't scheduled > legal/organization limits for a
	// week
	private boolean isManager;
	private Availability availability;
	private Business bus;
	private String currentSchedule;

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
	
	/*
	 * returns null if the employee does not exist in the database, otherwise,
	 * returns the Employee object with filled data fields.
	 */
	public static Employee loadFromID(int id) {
		String query = "SELECT * FROM " + getTableName() + " WHERE id=" + id;
		ArrayList<HashMap<String, String>> result = Database.executeSelectQuery(query);
		if (result.size() == 0)
			return null;
		HashMap<String, String> hm = result.get(0);
		String[] avail = Availability.loadAvailabilityFromID(id);
		boolean isManager;
		if (hm.get("is_manager").equals("1"))
			isManager = true;
		else
			isManager = false;
		Employee loaded = new Employee(Integer.parseInt(hm.get("id")), hm.get("fname"), hm.get("lname"), hm.get("email"), avail, isManager);
		Business bus = Business.loadFromID(Integer.parseInt(hm.get("business_id")));
		bus.addEmployee(loaded);
		loaded.setBusiness(bus);
		setCurrentSchedule(loaded);
		return loaded;
	}

	public void setBusiness(Business bus) {
		this.bus = bus;
	}
	
	private Business getBusiness() {
		return this.bus;
	}
	
	private static void setCurrentSchedule(Employee emp) {
		ArrayList<ArrayList<TimeSlot>> shiftList = emp.getBusiness().getEmployeesCurrentSchedule(emp.getID());
		String schedule = "";
		for (int i = 0; i < 7; i++) {
			//schedule += Day.get(i);
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
	
	public void setSched(String sched) {
		this.currentSchedule = sched;
	}
	
	public String getSchedule() {
		return this.currentSchedule;
	}

	/*
	 * If you are creating a brand new employee, use -1 for id. 
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
		
		boolean result;
		// UPDATE
		if (Database.tableContainsID(getTableName(), empID)) {
			result = Database.executeManipulateDataQuery(String.format(
					"UPDATE `%s`.`%s` SET `fname`='%s',`lname`='%s',`email`='%s',`business_id`=%d,`is_manager`=%d"
							+ " WHERE `id`=%d",
					Database.getName(), getTableName(), fname, lname, email, 0, empID, isManager ? 1 : 0));
		} else {

		// 0 is placeholder for business_id for now, since there is no ID in
		// business ATM
		result = Database.executeManipulateDataQuery(String.format(
				"INSERT INTO `%s`.`%s` " + "(`id`, `fname`, `lname`, `email`, `business_id`, `is_manager`)"
						+ " VALUES ('%d', '%s', '%s', '%s', %d, %d)",
				Database.getName(), getTableName(), empID, fname, lname, email, 0, isManager ? 1 : 0));
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
