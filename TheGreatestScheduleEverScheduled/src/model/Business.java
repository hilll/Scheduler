package model;
import java.util.ArrayList;
import java.util.HashMap;

import controller.Database;

public class Business {
	
	private final int MAX_EMP_HOURS = 100; // (25 hours per week * 4 time blocks per hour)
	private final int MAX_MANAGER_HOURS = 160; // (40 hours per week * 4 time blocks per hour)
	private String busName;
	private Schedule masterSchedule;
	private Schedule currentSchedule;
	private ArrayList<Schedule> previousSchedules;
	private ArrayList<Employee> staff;
	private int id;
	
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
	
	/*
	 * returns null if the business does not exist in the database, otherwise,
	 * returns the Employee object with filled data fields.
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
		return loaded;
	}
	
	private static void fillSchedules(Business bus) {
		String query = "SELECT * FROM " + Schedule.getTableName() + " WHERE id=" + bus.getID();
		ArrayList<HashMap<String, String>> result = Database.executeSelectQuery(query);
		if (result.size() == 0)
			return;
		HashMap<String, String> hm;
		Schedule curr = null;
		for (int i = 0; i < result.size(); i++) {
			hm = result.get(i);
			curr = Schedule.loadFromID(Integer.parseInt(hm.get("business_id")));
			if (curr != null) {
				bus.addToPrevSchedules(curr);
			}
		}
		// last curr will be most recent schedule
		if (curr != null) {
			bus.setCurrectSchedule(curr);
			bus.setMasterSchedule(curr);
		}
	}
	
	public static String getTableName() {
		return "business";
	}
	
	public Schedule generateNewSchedule() {
		
		Schedule newSchedule = new Schedule();
		newSchedule.copyAllShiftsPool(getMasterSchedule());
		
		//Initialize employee block pools
		initializeStaffPools();
		
		int numBlocks = getMasterSchedule().getAllShiftsPool().size();
		int numRounds = 0;
		while(numBlocks > 0 && numRounds < (getMasterSchedule().getAllShiftsPool().size()-10) ){		//START TMA
			for(Employee emp : staff){
				if(numRounds < emp.getAvailability().getAvailabilityPool().size()){
					TimeSlot empChoice = emp.getAvailability().getAvailabilityPool().get(numRounds);
					TimeSlot shift = newSchedule.getTimeBlock(empChoice);
					if(shift.getEmployeeID() == -1){
						shift.setEmployeeID(emp.getID());
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
	 * Adds all of the availability blocks of each employee to the masterSchedule's blockPool
	 */
	private void initializeStaffPools() {
		for(Employee emp : staff){
			//Fill employee shift pool
			emp.getAvailability().fillBlockPool(getMasterSchedule());
			
			//Set preferences = RANDOMIZE
			emp.getAvailability().setSchedulePrefrences();
		}
	}

	public void createShift(int day, int start, int end, boolean isManager){
		getMasterSchedule().addTimeBlock(day, start, end, isManager, null);
		getMasterSchedule().fillCompanyPool();
	}

	public void removeTimeBlock(int slotID) {
		getMasterSchedule().removeTimeBlock(slotID);
		getMasterSchedule().fillCompanyPool();
	}
	
	public void addEmployee(Employee emp) {
		staff.add(emp);
	}
	
	/*
	 * removes employee & returns true if it exists in the business's staff,
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
	
	public Schedule getMasterSchedule() {
		return masterSchedule;
	}
	
	public Schedule getCurrentSchedule() {
		return currentSchedule;
	}

	public void setMasterSchedule(Schedule masterSchedule) {
		this.masterSchedule = masterSchedule;
	}
	
	public void setCurrectSchedule(Schedule sched) {
		this.currentSchedule = sched;
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
	
	public int getID() {
		return id;
	}
	
	public void addToPrevSchedules(Schedule sched) {
		this.previousSchedules.add(sched);
	}

	public String getName() {
		return this.busName;
	}
	
	public void setName(String newName) {
		this.busName = newName;
	}
	
	public ArrayList<Employee> getStaff() {
		return this.staff;
	}
	
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

	// delete Business from DB
	public boolean delete() {
		for (Schedule sched : this.previousSchedules)
			sched.delete();
		this.getCurrentSchedule().delete();
		this.getMasterSchedule().delete();
		return Database.executeManipulateDataQuery(
				String.format("DELETE FROM `%s`.`%s` WHERE `id`='%d'", Database.getName(), Business.getTableName(), getID()));
	}

	// save Business into DB via insert or update
	public boolean save(int businessID) {

		boolean result = true;
		boolean slotResult;
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

		for (Schedule sched : this.previousSchedules) {
			System.out.println(sched);
			sched.save(businessID);
		}
		this.getCurrentSchedule().save(businessID);
		this.getMasterSchedule().save(businessID);

		return result;
	}
}
