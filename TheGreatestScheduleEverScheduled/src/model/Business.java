package model;
import java.util.ArrayList;
import java.util.HashMap;

import controller.Database;

/**
 * The Business object has a list of all of its schedules (previousSchedules). A
 * copy of the most recent schedule is stored at masterSchedule. Changes to the 
 * schedule (addNewShift, deleteShift) happen to the masterSchedule. When you 
 * generate a new schedule (using generateNewSchedule), the algorithm seeks to 
 * fill all of the masterSchedule's shifts with the Employees on staff. Changes 
 * do not occur to the masterSchedule, however. The currentSchedule instance variable
 * stores the newly generated shift. Every time you call generateNewSchedule, the 
 * currentSchedule is overwritten. If you call saveCurrentSchedule, the masterSchedule
 * is set to the currentSchedule. When you save the business object, the masterSchedule
 * is saved into the schedule and master_schedule tables. So, if you have added or deleted 
 * shifts, but you haven't saved a newly generated schedule, these shift changes persist 
 * until the next time the manager is working on the schedule, because the masterSchedule,
 * which already exists in the Database, is updated to reflect any changes. If you have saved a 
 * newly generated schedule, then it is inserted into the Database as a completely new 
 * Schedule, because its schedule id won't exist in the Database yet. 
 */
public class Business {
	
	// the name of this Business
	private String busName;
	// this Business's id
	private int id;
	// this business's most recently saved Schedule
	private Schedule masterSchedule;
	// this business's current Schedule draft
	private Schedule currentSchedule;
	// a list of this Business's previous schedules
	private ArrayList<Schedule> previousSchedules;
	// a list of this Business's staff
	private ArrayList<Employee> staff;
	
	/**
	 * If you are creating a new Business, use -1 for the id, and the correct id 
	 * will be grabbed from the table to use.
	 */
	public Business(int id, String name) {
		if (id == -1) {
			id = Database.getNextIDForTable(getTableName()); 
			if (id < 0) { 
				System.out.println("an error occurred while getting next ID return false");
			}
		}
		this.id = id;
		this.busName = name;
		this.setMasterSchedule(new Schedule());
		this.currentSchedule = new Schedule();
		this.previousSchedules = new ArrayList<Schedule>();
		this.staff = new ArrayList<Employee>();
	}
	
	/**
	 * Returns null if the Business with that id does not exist in the 
	 * database, otherwise, returns the Business object with filled data fields.
	 */
	public static Business loadFromID(int id) {
		String query = "SELECT * FROM " + getTableName() + " WHERE id=" + id;
		ArrayList<HashMap<String, String>> result = Database.executeSelectQuery(query);
		if (result.size() == 0)
			return null;
		HashMap<String, String> hm = result.get(0);
		String busName = hm.get("name");
		Business loaded = new Business(id, busName);
		fillSchedules(loaded);
		fillStaffPool(loaded);
		return loaded;
	}
	
	/*
	 * Helper method for the loadFromID class. Grabs all of the employees matching 
	 * this Business id from the Database.
	 */
	private static void fillStaffPool(Business bus) {
		String query = "SELECT * FROM " + Employee.getTableName() + 
				" WHERE business_id=" + bus.getID();
		ArrayList<HashMap<String, String>> result = Database.executeSelectQuery(query);
		if (result.size() == 0)
			return;
		for (int i = 0; i < result.size(); i++) {
			Employee.loadFromID(Integer.parseInt(result.get(i).get("id")), bus);
		}
	}
	
	/*
	 * Helper method for the loadFromID class. Grabs all of the schedules 
	 * matching the Business from the Database.
	 */
	private static void fillSchedules(Business bus) {
		String query = "SELECT * FROM " + Schedule.getTableName() + " WHERE business_id=" + bus.getID();
		ArrayList<HashMap<String, String>> result = Database.executeSelectQuery(query);
		if (result.size() == 0) {
			System.out.println("Result was 0.");
			return;
		}
		HashMap<String, String> hm;
		Schedule curr = null;
		for (int i = 0; i < result.size(); i++) {
			hm = result.get(i);
			curr = Schedule.loadFromID(Integer.parseInt(hm.get("id")));
			if (curr != null) {
				bus.addToPrevSchedules(curr);
			}
		}
		// last curr will be most recent schedule
		if (curr != null) {
			//bus.setCurrectSchedule(curr);
			bus.setMasterSchedule(curr);
		}
	}
	
	/**
	 * Returns the Business table name.
	 */
	public static String getTableName() {
		return "business";
	}
	
	/**
	 * Generates and returns a new Schedule object, after setting this Business's
	 * current schedule to the new Schedule. To do this, it attempts to fill all of 
	 * the shifts from the masterSchedule using this Business's staff's availability.
	 */
	public Schedule generateNewSchedule() {
		
		this.getMasterSchedule().fillCompanyPool();
		Schedule newSchedule = new Schedule();
		newSchedule.copyAllShiftsPool(getMasterSchedule());
		
		//Initialize employee availability pools
		initializeStaffPools();
		
		int numBlocks = getMasterSchedule().getAllShiftsPool().size();
		
		int numRounds = 0;
		while(numBlocks > 0 && numRounds < (getMasterSchedule().getAllShiftsPool().size())) { //-this.staff.size())) { //START TMA
			for(Employee emp : staff){
				if(numRounds < emp.getAvailability().getAvailabilityPool().size()) {
					TimeSlot empChoice = emp.getAvailability().getAvailabilityPool().get(numRounds);
					TimeSlot shift = newSchedule.getTimeBlock(empChoice);
					if(shift.getEmployeeID() == 7){
						System.out.println("Inside the if shift.getEmpID == 7 block");
						shift.setEmployeeID(emp.getID());
						shift.setEmployee(emp);
						numBlocks--;
					}
				}
			}
			numRounds++;
		}
		
		if(numBlocks != 0){
			System.out.println("SOME SHIFTS LEFT OPEN");
		}
		
		//Fill TimeBlock
		newSchedule.buildTimeBlocksFromPool();
		
		//Make it the new current schedule
		this.currentSchedule = newSchedule;
		
		return newSchedule;
	}
	
	/*
	 * Fills the availability pool of each Employee on staff with those shifts
	 * from the master schedule which fit within the Employee's availability
	 */
	private void initializeStaffPools() {
		for(Employee emp : staff){
			//Fill employee shift pool
			emp.getAvailability().fillAvailabilityPool(getMasterSchedule());
			
			//Set preferences = RANDOMIZE
			emp.getAvailability().setSchedulePrefrences();
		}
	}

	/**
	 * Create a new shift (TimeSlot) in the master schedule with the given 
	 * parameters. Side effect: saves the changed schedule to the database.
	 */
	public void createShift(int day, int start, int end, boolean isManager){
		if (Employee.getLoggedIn().getIsManager()) {
			System.out.println(Employee.getLoggedIn().getFullName() + " is a manager, so the shift will be added.");
		} else {
			System.out.println(Employee.getLoggedIn().getFullName() + " is not a manager, so they are unable to add a shift to the schedule.");
			return;
		}
		getMasterSchedule().addTimeBlock(day, start, end, isManager, null);
		getMasterSchedule().fillCompanyPool();
		getMasterSchedule().save(this.getID());
	}

	/**
	 * Removes the shift (TimeBlock) with the given id from the master schedule's
	 * block pool. Side effect: saves the changed schedule to the database.
	 */
	public void removeShift(int slotID) {
		if (Employee.getLoggedIn().getIsManager()) {
			System.out.println(Employee.getLoggedIn().getFullName() + " is a manager, so the shift will be deleted.");
		} else {
			System.out.println(Employee.getLoggedIn().getFullName() + " is not a manager, so they are unable to delete a shift from the schedule.");
			return;
		}
		getMasterSchedule().removeTimeBlock(slotID);
		getMasterSchedule().fillCompanyPool();
		getMasterSchedule().save(this.getID());
	}
	
	/**
	 * Adds the given Employee to this Business's staff.
	 */
	public void addEmployee(Employee emp) {
		staff.add(emp);
	}
	
	/**
	 * Removes employee & returns true if it exists in the business's staff,
	 * else returns false
	 */
	public boolean removeEmployee(Employee emp) {
		for (Employee e : staff) {
			if (e.equals(emp)) {
				staff.remove(e);
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Returns the Master Schedule for this Business.
	 */
	public Schedule getMasterSchedule() {
		return masterSchedule;
	}
	
	/**
	 * Returns the Current (unsaved) Schedule for this Business.
	 */
	public Schedule getCurrentSchedule() {
		return currentSchedule;
	}

	/*
	 * Sets the master schedule
	 */
	private void setMasterSchedule(Schedule masterSchedule) {
		this.masterSchedule = masterSchedule;
	}
	
	/**
	 * Returns the list of previous schedules belonging to this Business.
	 */
	public ArrayList<Schedule> getPrevSchedules() {
		return this.previousSchedules;
	}
	
	/**
	 * Sets this business's list of staff members equal to staff parameter. 
	 * Overwrites any current staff members. For each Employee in staff
	 * ArrayList, it changes their Business to the current business, 
	 * overwriting their existing business if they have one. 
	 * 
	 */
	public void setStaff(ArrayList<Employee> staff) {
		for (Employee emp : staff) {
			emp.setBusiness(this);
		}
		this.staff = staff;
	}
	
	/**
	 * Returns this Business's id.
	 */
	public int getID() {
		return id;
	}
	
	/*
	 * Adds sched to this Business's list of previous schedules.
	 */
	private void addToPrevSchedules(Schedule sched) {
		this.previousSchedules.add(sched);
	}

	/**
	 * Returns this Business's name.
	 */
	public String getName() {
		return this.busName;
	}
	
	/**
	 * Sets this Business's name to newName.
	 */
	public void setName(String newName) {
		this.busName = newName;
	}
	
	/**
	 * Returns this Business's staff list.
	 */
	public ArrayList<Employee> getStaff() {
		return this.staff;
	}
	
	/**
	 * Returns the scheduled shifts of the Employee with empID.
	 */
	public ArrayList<ArrayList<TimeSlot>> getEmployeesCurrentSchedule(int empID) {
		ArrayList<ArrayList<TimeSlot>> sched = new ArrayList<>();
		for (int i = 0; i < 7; i++) {
			sched.add(new ArrayList<TimeSlot>());
		}
		for (TimeSlot slot : this.masterSchedule.getAllShiftsPool()) {
			if (slot.getEmployeeID() == empID)
				sched.get(slot.getDay()).add(slot);
		}
		return sched;
	}
	
	public boolean saveCurrentSchedule() {
		if (this.getCurrentSchedule() != null) {
			this.setMasterSchedule(this.getCurrentSchedule());
			this.getMasterSchedule().save(this.getID());
			for (Employee e : this.getStaff()) {
				Employee.setCurrentSchedule(e);
			}
			return true;
		} else {
			System.out.println("A new schedule has not been generated yet. Doesn't make sense to save it.");
			return false;
		}
	}

	/**
	 * Deletes this Business and all of its associated Schedules from the 
	 * Database.
	 */
	public boolean delete() {
		for (Schedule sched : this.previousSchedules)
			Schedule.delete(sched.getID());
		Schedule.delete(getMasterSchedule().getID());
		return Database.executeManipulateDataQuery(
				String.format("DELETE FROM `%s`.`%s` WHERE `id`='%d'", Database.getName(), Business.getTableName(), getID()));
	}

	/**
	 * Saves the Business with businessID, and all of its Schedules into the
	 * Database.
	 */
	public boolean save(int businessID) {

		boolean result = true;
		// UPDATE
		if (Database.tableContainsID(getTableName(), getID())) {
			result = Database.executeManipulateDataQuery(String.format(
					"UPDATE `%s`.`%s` SET `name`='%s'"
							+ " WHERE `id`=%d",
					Database.getName(), getTableName(), this.getName(), this.getID()));
		} else {
			result = Database.executeManipulateDataQuery(String.format(
					"INSERT INTO `%s`.`%s` " + "(`id`, `name`)"
							+ " VALUES ('%d', '%s')",
							Database.getName(), getTableName(), this.getID(), this.getName()));
		}
		//this.getMasterSchedule().save(businessID);

		return result;
	}
}
