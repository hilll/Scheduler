package model;

import java.util.ArrayList;
import java.util.HashMap;

import controller.Database;

public class Schedule {
	
	private ArrayList<ArrayList<TimeSlot>> shiftsByDay;
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
	
	public static String getTableName() {
		return "schedule";
	}
	
	public static String getMasterScheduleTableName() {
		return "master_schedule";
	}
	
	public void setID(int id) {
		this.id = id;
	}
	
	public int getID() {
		return this.id;
	}
	
	/*
	 * Adds slot to the appropriate day of the shiftsByDay list.
	 * Use this to add an existing shift to the Schedule when you're loading from the database.
	 */
	public void addShift(TimeSlot slot) {
		shiftsByDay.get(slot.getDay()).add(slot);
		allShiftsPool.add(slot);
	}
	
	/*
	 * Use this to create an entirely new shift.
	 */
	public void addTimeBlock( int day, int start, int end, boolean isManager, Employee emp) {
		TimeSlot slot = new TimeSlot(TimeSlot.getNewTimeSlotID(), null, day, start, end, isManager);
		slot.setEmployeeID(emp.getID());
		this.shiftsByDay.get(day).add(slot);
	}
	
	/*
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
	
	public Schedule makeNewSchedule(Employee[] staff){
		Schedule newSchedule = new Schedule();
		return newSchedule;	
	}
	
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
	
	public void buildTimeBlocksFromPool(){
		this.shiftsByDay = new ArrayList<ArrayList<TimeSlot>>(7);
		for(int i = 0; i < 7; ++i){
			shiftsByDay.add(new ArrayList<TimeSlot>());
		}
		for(TimeSlot block : this.allShiftsPool){
			this.shiftsByDay.get(block.getDay()).add(block);
		}
	}
	
	public void copyAllShiftsPool(Schedule that) {
		for(TimeSlot slot : that.allShiftsPool){
			//this.allShiftsPool.add(new TimeSlot(slot.getID(), slot.getTimeAsString(), slot.getDay(), slot.getStart(), slot.getEnd(), slot.getIsManagerTimeSlot()));
			this.allShiftsPool.add(slot);
		}
	}
	
	public TimeSlot getTimeBlock(TimeSlot slot){
		for(TimeSlot block : this.allShiftsPool){
			if(block.isEqualByID(slot)){
				return block;
			}
		}
		return null;
	}
	
	public void printSchedule(){
		System.out.println("\t| 0  1  2  3  4  5  6  7  8  9 10 11 12 13 14 15 16 17 18 19 20 21 22 23 ");
		String dash = "================================================================================";
		System.out.println( dash);
		String[] days = {"Sun\t","Mon\t", "Tues\t", "Wed\t", "Thur\t", "Fri\t", "Sat\t"};
		for(int i = 0; i < days.length; i++){
			
			//Print day name if first time block, else aline with header
			boolean firstBlock = true;
			for(int block = 0; block < this.shiftsByDay.get(i).size(); block++){
				if(firstBlock){
					System.out.print(days[i]+"|");
					
					firstBlock = false;
				}
				else{
					System.out.print("\t|");
				}
				
				boolean inBlock= false;
				for(int c = 0 ; c < 75; c+=3){
					if(this.shiftsByDay.get(i).get(block).getStart() == (c/3)){
						System.out.print("|");
						
						if(this.shiftsByDay.get(i).get(block).getEmployeeID() != -1){
							String name = this.shiftsByDay.get(i).get(block).getEmployee().getFullName() + "----------";
							System.out.print(name.substring(0, 8));
						}
						else
							System.out.printf("id=%2d---", this.shiftsByDay.get(i).get(block).getID());
						c+=8;
						inBlock = true;
					}
					if(this.shiftsByDay.get(i).get(block).getEnd() == (c/3)+1){
						System.out.print("|\n");
						inBlock = false;
						break;
					}
					if(inBlock){
						System.out.print("---");
					}
					else{
						System.out.print("   ");
					}
				}
				
			}
			System.out.println( dash);
		}
		System.out.println();
	}

	public ArrayList<TimeSlot> getAllShiftsPool() {
		return allShiftsPool;
	}

	public ArrayList<ArrayList<TimeSlot>> getShiftsByDay() {
		return shiftsByDay;
	}

	// delete Schedule from DB: schedule and master_schedule tables
	public boolean delete() {
		Database.executeManipulateDataQuery(
				String.format("DELETE FROM `%s`.`%s` WHERE `sched_id`='%d'", Database.getName(), getMasterScheduleTableName(), getID()));
		return Database.executeManipulateDataQuery(
				String.format("DELETE FROM `%s`.`%s` WHERE `id`='%d'", Database.getName(), getTableName(), getID()));
	}

	// save Schedule into DB via insert or update
	public boolean save(int businessID) {

		boolean result = true;
		boolean slotResult;
		// UPDATE
		if (Database.tableContainsID(getTableName(), getID())) {
			// update TimeSlots in the master_schedule table
			for (TimeSlot slot : allShiftsPool) {
				slotResult = slot.save(slot.getID());
				if (!slotResult)
					result = false;
			}
		} else {
			result = Database.executeManipulateDataQuery(String.format(
					"INSERT INTO `%s`.`%s` " + "(`id`, `business_id`)"
							+ " VALUES ('%d', '%d')",
							Database.getName(), getTableName(), id, businessID));
			for (TimeSlot slot : allShiftsPool) {
				slotResult = slot.save(slot.getID());
				if (!slotResult)
					result = false;
			}
		}
		return result;
	}
}
