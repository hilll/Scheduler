package model;

import java.util.ArrayList;
import java.util.HashMap;

import controller.Database;

public class Schedule {
	
	// a list of lists of shifts, organized by day
	private ArrayList<ArrayList<TimeSlot>> shiftsByDay;
	// a list of all the shifts for the schedule. Used for scheduling.
	private ArrayList<TimeSlot> allShiftsPool;
	private int id;
	
	public Schedule(){
		int id = Database.getNextIDForTable(getTableName()); 
		if (id < 0) { 
			System.out.println("an error occurred while getting next ID return false");
		} else {
		    this.id = id;
		}
		this.shiftsByDay = new ArrayList<ArrayList<TimeSlot>>(7);
		for(int i = 0; i < 7; ++i){
			shiftsByDay.add(new ArrayList<TimeSlot>());
		}
		this.allShiftsPool = new ArrayList<>();
	}
	
	/**
	 * Load and return the Schedule with the given id from the Database, or return
	 * null if a Schedule with that id doesn't exist in the Database.
	 */
	public static Schedule loadFromID(int id) {
		String query = "SELECT * FROM " + getMasterScheduleTableName() + " WHERE sched_id=" + id;
		ArrayList<HashMap<String, String>> result = Database.executeSelectQuery(query);
		if (result.size() == 0)
			return null;
		Schedule loaded = new Schedule();
		HashMap<String, String> hm;
		for (int i = 0; i < result.size(); i++) {
			hm = result.get(i);
			loaded.addShift(TimeSlot.loadFromID(Integer.parseInt(hm.get("timeslot_id")), Integer.parseInt(hm.get("emp_id"))));
		}
		loaded.setID(id);
		return loaded;
	}
	
	/**
	 * Returns the table name for Schedule.
	 */
	public static String getTableName() {
		return "schedule";
	}
	
	/**
	 * Returns the table name for Master Schedule.
	 * @return
	 */
	public static String getMasterScheduleTableName() {
		return "master_schedule";
	}
	
	/**
	 * Sets this Schedule's id to id. Should not be called outside
	 * of the loadFromID function.
	 */
	public void setID(int id) {
		this.id = id;
	}
	
	/**
	 * Returns the id for this Schedule.
	 */
	public int getID() {
		return this.id;
	}
	
	/**
	 * Adds slot to the appropriate day of the shiftsByDay list.
	 * Use this to add an existing shift to the Schedule when you're 
	 * loading from the database.
	 */
	public void addShift(TimeSlot slot) {
		shiftsByDay.get(slot.getDay()).add(slot);
		allShiftsPool.add(slot);
	}
	
	/**
	 * Use this to create an entirely new shift.
	 */
	public void addTimeBlock(int day, int start, int end, boolean isManager, Employee emp) {
		TimeSlot slot = new TimeSlot(TimeSlot.getNewTimeSlotID(), null, day, start, end, isManager);
		if (emp != null)
			slot.setEmployeeID(emp.getID());
		else
			slot.setEmployeeID(7);
		this.shiftsByDay.get(day).add(slot);
	}
	
	/**
	 * returns true if the TimeBlock with slotID is successfully removed, false if it 
	 * did not exist.
	 */
	public boolean removeTimeBlock(int slotID){
		for(int day = 0; day < 7; day++){
			for(int i = 0 ; i < this.shiftsByDay.get(day).size(); i++){
				if(this.shiftsByDay.get(day).get(i).getID() == slotID){
					this.shiftsByDay.get(day).remove(i);
					return true;
				}
			}
		}
		return false;		
	}
	
	/**
	 * Fills the pool of shifts so that a new Schedule can be made.
	 */
	public void fillCompanyPool(){
		//empty old pool
		this.allShiftsPool.clear();
		
		//add Time Blocks back in
		for(int day = 0; day < 7; day++){	//time blocks this has
			for(TimeSlot thisSlot : this.shiftsByDay.get(day)){
					this.allShiftsPool.add(thisSlot);
			}	
		}
	}
	
	/**
	 * Fills the shiftsByDay by assigning shifts from the allShiftsPool
	 * to the appropriate day.
	 */
	public void buildTimeBlocksFromPool(){
		this.shiftsByDay = new ArrayList<ArrayList<TimeSlot>>(7);
		for(int i = 0; i < 7; ++i){
			shiftsByDay.add(new ArrayList<TimeSlot>());
		}
		for(TimeSlot block : this.allShiftsPool){
			this.shiftsByDay.get(block.getDay()).add(block);
		}
	}
	
	/**
	 * Copies the shifts from one that Schedule into this shiftPool.
	 */
	public void copyAllShiftsPool(Schedule that) {
		for(TimeSlot slot : that.allShiftsPool){
			slot.setEmployeeID(7);
			this.allShiftsPool.add(slot);
		}
	}
	
	/**
	 * Returns the matching TimeSlot from this allShiftsPool if it 
	 * exists, null if it doesn't.
	 */
	public TimeSlot getTimeBlock(TimeSlot slot){
		for(TimeSlot block : this.allShiftsPool){
			if(block.isEqualByID(slot)){
				return block;
			}
		}
		return null;
	}
	
	public String toString() {
		
		String result = "";
		
		for (int i = 0; i < 7; i++) {
			String dayLabel = "Day " + i + ":\n";
			result += dayLabel;
			for (TimeSlot ts : this.shiftsByDay.get(i)) {
				result += "\t";
				if (ts.getEmployee() != null) {
					result += ts.getEmployee().getFullName();
					if (ts.getEmployee().getIsManager()) 
						result += " (m)";
				} else {
					result += "Unassigned";
				}
				result += ": ";
				result += ts.toString();
				result += "\n";
			}
		}
		return result;
	}
	
	
//	/**
//	 * Prints out a textual representation of this Schedule.
//	 */
//	public void printSchedule(){	
//		
//		System.out.println("\t| 0  1  2  3  4  5  6  7  8  9 10 11 12 13 14 15 16 17 18 19 20 21 22 23 24 25 26 27 28 29 30 31 32 33 34 35 36 37 38 39 40 41 42 43 44 45 46 47 48 49 50 51 52 53 54 55 56 57 58 59 60 61 62 63 64 65 66 67 68 69 70 71 72 73 74 75 76 77 78 79 80 81 82 83 84 85 86 87 88 89 90 91 92 93 94 95");
//		String dash = "========================================================================================================================================================================================================================================================================================================";
//		System.out.println( dash);
//		String[] days = {"Sun\t","Mon\t", "Tues\t", "Wed\t", "Thur\t", "Fri\t", "Sat\t"};
//		for(int i = 0; i < days.length; i++){
//			
//			//Print day name if first time block, else aline with header
//			boolean firstBlock = true;
//			for(int block = 0; block < this.shiftsByDay.get(i).size(); block++){
//				if(firstBlock){
//					System.out.print(days[i]+"|");
//					
//					firstBlock = false;
//				}
//				else{
//					System.out.print("\t|");
//				}
//				
//				boolean inBlock= false;
//				for(int c = 0 ; c < 75; c+=3){
//					if(this.shiftsByDay.get(i).get(block).getStart() == (c/3)){
//						System.out.print("|");
//						
//						if(this.shiftsByDay.get(i).get(block).getEmployeeID() != -1){
//							String name = this.shiftsByDay.get(i).get(block).getEmployee().getFullName() + "----------";
//							System.out.print(name.substring(0, 8));
//						}
//						else
//							System.out.printf("id=%2d---", this.shiftsByDay.get(i).get(block).getID());
//						c+=8;
//						inBlock = true;
//					}
//					if(this.shiftsByDay.get(i).get(block).getEnd() == (c/3)+1){
//						System.out.print("|\n");
//						inBlock = false;
//						break;
//					}
//					if(inBlock){
//						System.out.print("---");
//					}
//					else{
//						System.out.print("   ");
//					}
//				}
//				
//			}
//			System.out.println( dash);
//		}
//		System.out.println();
//	}

	/**
	 * Returns the allShiftsPool from this Schedule.
	 */
	public ArrayList<TimeSlot> getAllShiftsPool() {
		return allShiftsPool;
	}

	/**
	 * Returns the shiftsByDay from this Schedule.
	 */
	public ArrayList<ArrayList<TimeSlot>> getShiftsByDay() {
		return shiftsByDay;
	}

	/**
	 * Deletes the Schedule with id from the Database.
	 */
	public static boolean delete(int id) {
		Database.executeManipulateDataQuery(
				String.format("DELETE FROM `%s`.`%s` WHERE `sched_id`='%d'", Database.getName(), getMasterScheduleTableName(), id));
		return Database.executeManipulateDataQuery(
				String.format("DELETE FROM `%s`.`%s` WHERE `id`='%d'", Database.getName(), getTableName(), id));
	}

	/**
	 * Saves this Schedule into the Database by updating or inserting it.
	 */
	public boolean save(int businessID) {

		boolean result = true;
		boolean slotResult;
		// UPDATE
		if (Database.tableContainsID(getTableName(), getID())) {
			// update TimeSlots in the master_schedule table
			for (TimeSlot slot : allShiftsPool) {
				slotResult = slot.save(this.getID());
				if (!slotResult)
					result = false;
			}
		} else {
			// INSERT
			result = Database.executeManipulateDataQuery(String.format(
					"INSERT INTO `%s`.`%s` " + "(`id`, `business_id`)"
							+ " VALUES ('%d', '%d')",
							Database.getName(), getTableName(), id, businessID));
			for (TimeSlot slot : allShiftsPool) {
				slotResult = slot.save(this.getID());
				if (!slotResult)
					result = false;
			}
		}
		return result;
	}
}
