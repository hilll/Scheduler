package model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

import controller.Database;

public class Availability {
	
	public static String getTableName() {
		return "availability";
	}

	// forgive the ugliness. When a new employee is created, its availability is auto-set to this to start with
	private final String[] starterAvailability = { "111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111",
												   "111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111",
			                                       "111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111",
			                                       "111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111",
			                                       "111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111",
			                                       "111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111",
			                                       "111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111",	   
												 };
	// we can change this constant to alter the shortest shifts an employee will be scheduled for
	private final int MIN_SHIFT_LENGTH = 1; // hours * (4 blocks per hour)
	// array of lists (one for each day of the week) of TimeBlocks employee is available to work
	private ArrayList<ArrayList<TimeSlot>> availabilityByDay;
	//private ArrayList<TimeSlot>[] timeBlocks;
	// list of all shifts from the schedule employee would be able to take
	private ArrayList<TimeSlot> availabilityPool;
	// list of time blocks during which employee has stated he/she is unavailable:
	private ArrayList<TimeSlot> unavailabilityList; 
	private int totalAvailabilityBlocks;
	private int totalUnavailabilityBlocks;
	private String[] availabilityStrings; // 7 strings of 96 characters: '1's (available) and '0's (unavailable)
	private boolean isManager;
	
	public Availability(String[] availability, boolean isManager){
		if (availability == null) {
			this.availabilityStrings = starterAvailability;
		} else {
			this.availabilityStrings = availability;
		}
		this.isManager = isManager;
		this.availabilityByDay = new ArrayList<ArrayList<TimeSlot>>(7);
		for(int i = 0; i < 7; ++i){
			availabilityByDay.add(new ArrayList<TimeSlot>());
		}
		this.availabilityPool = new ArrayList<>();
		this.unavailabilityList = new ArrayList<>();
		for (int i = 0; i < 7; i++)
			fillPools(i);
		this.totalAvailabilityBlocks = 0;
		this.totalUnavailabilityBlocks = 0;
	}
	
	private void fillPools(int day) {
		String daysAvailability = availabilityStrings[day];
		int streakType = -1; // 1 if currently counting available slots, 0 otherwise
		int unavailableStart = -1;
		int unavailableEnd = -1;
		int availableStart = -1;
		int availableEnd = -1;
		for (int c = 0; c < daysAvailability.length(); c++) {
			int curr = Character.getNumericValue(daysAvailability.charAt(c));
			if (curr == -1) {
				System.out.println("There is an error in the database string representing employee availability.\n");
				System.exit(-1);
			}
			if (streakType != curr) { // either start of string, or switching from avail to unavail, or from unavail to avail
				if (curr == 1) { // last streak was of 0s, or it's the start of the string
					if (c == 0) {
						availableStart = 0;
					} else { // not the beginning of string
						unavailableEnd = c - 1;
						addUnavailability(day, unavailableStart, unavailableEnd);
						unavailableStart = -1;
						unavailableEnd = -1;
						availableStart = c;
					}
				} else { // last streak was of 1s, or it's the start of the string
					if (c == 0) {
						unavailableStart = 0;
					} else { // not the beginning of string
						availableEnd = c - 1;
						if (availableEnd - availableStart >= MIN_SHIFT_LENGTH) {
							addAvailability(day, availableStart, availableEnd);
						}
					}
				}
				streakType = curr;
			} else {
				if (c == daysAvailability.length() - 1) {
					if (streakType == 0) {
						unavailableEnd = c;
						addUnavailability(day, unavailableStart, unavailableEnd);
					} else {
						availableEnd = c;
						addAvailability(day, availableStart, availableEnd);
					}
				}
				continue;
			}
		}
	}
	
	public void addUnavailability(int day, int start, int end) {
		TimeSlot slot = new TimeSlot(-1, day, start, end, isManager);
		unavailabilityList.add(slot);
	}
	
	public void removeUnavailability(int day, int start, int end) {
		for (TimeSlot ts : unavailabilityList) {
			if (ts.getDay() == day) {
				if (ts.getStart() == start) {
					if (ts.getEnd() == end) {
						unavailabilityList.remove(ts);
						return;
					}
				}
			}
		}
	}
	
	public void addAvailability(int day, int start, int end) {
		TimeSlot slot = new TimeSlot(-1, day, start, end, isManager);
		this.availabilityByDay.get(day).add(slot);
	}
	
	/*
	 * returns true if the TimeBlock with slotID is successfully removed, false if it 
	 * did not exist.
	 */
	public boolean removeTimeBlock(int slotID){
		for(int day = 0; day < 7; day++){
			for(int i = 0 ; i < this.availabilityByDay.get(day).size(); i++){
				if(this.availabilityByDay.get(day).get(i).getID() == slotID){
					this.availabilityByDay.get(day).remove(i);
					return true;
				}
			}
		}
		return false;		
	}

	public void setSchedulePrefrences(){
		//Randomize
		long seed = System.nanoTime();
		Collections.shuffle(this.availabilityPool, new Random(seed));
	}
	
	public TimeSlot getTimeBlock(TimeSlot slot){
		for(TimeSlot block : this.availabilityPool){
			if(block.isEqual(slot)){
				return block;
			}
		}
		return null;
	}

	public ArrayList<TimeSlot> getAvailabilityPool() {
		return availabilityPool;
	}
	
	public void fillBlockPool(Schedule company){
		this.availabilityPool = new ArrayList<TimeSlot>();
		for(int day = 0; day < this.availabilityByDay.size(); day++){	//time blocks this has
			for(TimeSlot thisSlot : this.availabilityByDay.get(day)){
				for(TimeSlot thatSlot : company.getShiftsByDay().get(day)){	//time blocks that has
					if(thisSlot.canFit(thatSlot)){
						this.availabilityPool.add(thatSlot);		//Put shift in emp pool
					}
				}
			}
		}
	}
	
	public String[] getAvailabilityStrings() {
		return availabilityStrings;
	}
	
	public ArrayList<ArrayList<TimeSlot>> getAvailabilityByDay() {
		return availabilityByDay;
	}
	
	public ArrayList<TimeSlot> getUnavailabilityList() {
		return unavailabilityList;
	}

	// delete Availability from DB
	public boolean delete(int empID) {
		return Database.executeManipulateDataQuery(
				String.format("DELETE FROM `%s`.`%s` WHERE `emp_id`='%d'", Database.getName(), getTableName(), empID));
	}

	// save Employee into DB via insert or update
	public boolean save(int empID) {
		// UPDATE
		if (Database.tableContainsID(getTableName(), empID)) {
			return Database.executeManipulateDataQuery(String.format(
					"UPDATE `%s`.`%s` SET `sunday`='%s',`monday`='%s',`tuesday`='%s',`wednesday`='%s',`thursday`='%s',`friday`='%s',`saturday`='%s'" + " WHERE `emp_id`=%d",
					Database.getName(), getTableName(), availabilityStrings[0], availabilityStrings[1], availabilityStrings[2], availabilityStrings[3],
					availabilityStrings[4], availabilityStrings[5], availabilityStrings[6], empID));
		}

		// INSERT
		int newID = Database.getNextIDForTable(Employee.getTableName());
		if (newID < 0) {
			// an error occurred while getting next ID
			return false;
		}

		// 0 is placeholder for business_id for now, since there is no ID in
		// business ATM
		return Database.executeManipulateDataQuery(String.format(
				"INSERT INTO `%s`.`%s` " + "(`emp_id`, `sunday`, `monday`, `tuesday`, `wednesday`, `thursday`, `friday`, `saturday`)"
						+ " VALUES ('%d', '%s', '%s', '%s', '%s', '%s', '%s')",
						Database.getName(), getTableName(), newID, availabilityStrings[0], availabilityStrings[1], availabilityStrings[2], availabilityStrings[3],
						availabilityStrings[4], availabilityStrings[5], availabilityStrings[6]));
	}
}
