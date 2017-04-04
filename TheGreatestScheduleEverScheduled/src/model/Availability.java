package model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Random;

import controller.Database;

/**
 * Availability 
 * 
 * Employees each have an availability. Stand-alone Availabilities should not be created,
 * when a new employee is created or loaded, the Availability information is automatically
 * populated.
 * 
 * @author Katie
 *
 */
public class Availability {

	// When a new employee is created, its availability is auto-set to this to start with
	private final String[] starterAvailability = { "111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111",
												   "111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111",
			                                       "111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111",
			                                       "111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111",
			                                       "111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111",
			                                       "111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111",
			                                       "111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111",	   
												 };
	// shortest shifts an employee will be scheduled for
	private final int MIN_SHIFT_LENGTH = 1; // hours * (4 blocks per hour)
	// array of lists (one for each day of the week) of TimeBlocks employee is available to work
	private ArrayList<ArrayList<TimeSlot>> availabilityByDay;
	// list of all shifts from the schedule employee would be able to take
	private ArrayList<TimeSlot> availabilityPool;
	// list of time blocks during which employee has stated he/she is unavailable
	private ArrayList<TimeSlot> unavailabilityList; 
	// 7 strings of 96 characters: '1's (available) and '0's (unavailable)
	private String[] availabilityStrings;
	// true if employee is a manager, false otherwise
	private boolean isManager;
	
	private HashMap<String, TimeSlot> availabilityHash;
	
	/**
	 * Arguments:
	 * 	availability: should be null for new Availability object,
	 * 			actual String[] of length 7 for loaded one.
	 * 	isMananger: whether the availability belongs to a manager
	 * 			or not.
	 */
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
		this.availabilityHash = new HashMap<>();
		this.unavailabilityList = new ArrayList<>();
		for (int i = 0; i < 7; i++)
			fillUnavailabilityAndAvailabilitiesByDay(i);
	}
	
	/**
	 * 
	 * @param id of the availability to load
	 * @return availability strings
	 */
	public static String[] loadAvailabilityFromID(int id) {
		String availQuery = "SELECT * FROM " + getTableName() + " WHERE emp_id=" + id; 
		ArrayList<HashMap<String, String>> aresult = Database.executeSelectQuery(availQuery);
		HashMap<String, String> am = aresult.get(0);
		String[] avail = new String[7];
		avail[0] = am.get("sunday");
		avail[1] = am.get("monday");
		avail[2] = am.get("tuesday");
		avail[3] = am.get("wednesday");
		avail[4] = am.get("thursday");
		avail[5] = am.get("friday");
		avail[6] = am.get("saturday");
		return avail;
	}
	
	/**
	 * 
	 * @return the table name for Availability
	 */
	public static String getTableName() {
		return "availability";
	}
	
	/*
	 * Processes a single day's availability string to populate the 
	 * unavailabilityList and availabilityByDay array with the appropriate 
	 * TimeSlots when an Employee is loaded or created.
	 */
	private void fillUnavailabilityAndAvailabilitiesByDay(int day) {
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
						unavailableStart = c;
						if (availableEnd - availableStart >= MIN_SHIFT_LENGTH) {
							addAvailability(day, availableStart, availableEnd);
						}
						availableStart = -1;
						availableEnd = -1;
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
	
	/*
	 * Should only be called from fillUnavailabilityAndAvailabilitiesByDay 
	 * method. It can't be used to change an Employee's unavailability, because
	 * it does not update availabilityStrings and thus won't be saved in the 
	 * database when the Employee logs out.
	 */
	private void addUnavailability(int day, int start, int end) {
		TimeSlot slot = new TimeSlot(-1, null, day, start, end, isManager);
		unavailabilityList.add(slot);
		availabilityHash.put(slot.toString(), slot);
	}
	
	/*
	 * Should only be called from fillUnavailabilityAndAvailabilitiesByDay 
	 * method. It can't be used to change an Employee's availability, because
	 * it does not update availabilityStrings and thus won't be saved in the 
	 * database when the Employee logs out.
	 */
	private void addAvailability(int day, int start, int end) {
		TimeSlot slot = new TimeSlot(-1, null, day, start, end, isManager);
		this.availabilityByDay.get(day).add(slot);
	}
	
	/**
	 * Returns a HashMap with keys being String representations of TimeSlots,
	 * and values being the TimeSlot itself.
	 */
	/*public HashMap<String, TimeSlot> getUnavailabilitySlots() {
		HashMap<String, TimeSlot> slots = new HashMap<String, TimeSlot>();
		for (TimeSlot ts : unavailabilityList) {
			slots.put(ts.toString(), ts);
		}
		return slots;
	}*/
	
	public ArrayList<String> getUnavailabilitySlots() {
		ArrayList<String> slots = new ArrayList<>();
		Collections.sort(unavailabilityList); // sort the list
		for (TimeSlot ts : unavailabilityList) {
			slots.add(ts.toString());
		}
		return slots;
	}
	
	/**
	 * Returns true is TimeSlot was successfully removed from this 
	 * unavailabilityList, false if it wasn't found.
	 */
	public boolean removeUnavailability(TimeSlot slot) {
		for (TimeSlot ts : unavailabilityList) {
			if (ts.isEqualByDayAndTimes(slot)) {
				unavailabilityList.remove(ts);
				availabilityHash.remove(ts.toString());
				updateAvailabilityStrings(ts.getDay(), ts.getStart(), ts.getEnd(), '1');
				return true;
			}
		}
		return false;
	}
	
	public boolean removeUnavailability(String slotStr) {
		return removeUnavailability(availabilityHash.get(slotStr));
	}
	
	/**
	 * Changes the value of an availability string. Inputs: day: which string to change,
	 * start and end: segment of the availability string to change, availability: what 
	 * to change the segment to. Does not check to make sure a change is actually being made.
	 */
	public boolean updateAvailabilityStrings(int day, int start, int end, char availability) {
		if (end < start)
			return false;
		char[] changeThis = availabilityStrings[day].toCharArray();
		for (int i = start; i <= end; i++) {
			changeThis[i] = availability;
		}
		availabilityStrings[day] = String.valueOf(changeThis);
		return true;
	}

	/**
	 * Currently randomly shuffles this Employee's availabilityPool. Maybe in
	 * future iterations it can be used to add employee preference into the 
	 * shift assignment process.
	 */
	public void setSchedulePrefrences() {
		long seed = System.nanoTime();
		Collections.shuffle(this.availabilityPool, new Random(seed));
	}

	/**
	 * Returns the Employee's availabilityPool.
	 */
	public ArrayList<TimeSlot> getAvailabilityPool() {
		return availabilityPool;
	}
	
	/**
	 * Fills the employee's availabilityPool with any shifts from company's 
	 * schedule that fit within the employee's availability constraints.
	 */
	public void fillAvailabilityPool(Schedule company) {
		this.availabilityPool = new ArrayList<TimeSlot>();
		for(int day = 0; day < this.availabilityByDay.size(); day++) { //time blocks this has
			for(TimeSlot thisSlot : this.availabilityByDay.get(day)) {
				for(TimeSlot thatSlot : company.getShiftsByDay().get(day)) { //time blocks that has
					if(thisSlot.canFit(thatSlot)) {
						this.availabilityPool.add(thatSlot); //Put shift in emp pool
						continue; // don't add the same availability slot twice
					}
				}
			}
		}
	}
	
	/**
	 * Returns the Employee's availabilityStrings.
	 */
	public String[] getAvailabilityStrings() {
		return availabilityStrings;
	}
	
	/**
	 * Returns this Employee's availabilityByDay.
	 */
	public ArrayList<ArrayList<TimeSlot>> getAvailabilityByDay() {
		return availabilityByDay;
	}
	
	/**
	 * Returns this Employee's unavailabilityList.
	 */
	public ArrayList<TimeSlot> getUnavailabilityList() {
		return unavailabilityList;
	}

	/**
	 * Deletes the Availability that belongs to the Employee with empID 
	 * from the Database.
	 */
	public boolean delete(int empID) {
		return Database.executeManipulateDataQuery(
				String.format("DELETE FROM `%s`.`%s` WHERE `emp_id`='%d'", Database.getName(), getTableName(), empID));
	}

	/**
	 * Saves or updates the Availability that belongs to the Employee with 
	 * empID in the Database.
	 */
	public boolean save(int empID) {
		// UPDATE
		if (Database.tableContainsID(getTableName(), empID)) {
			return Database.executeManipulateDataQuery(String.format(
					"UPDATE `%s`.`%s` SET `sunday`='%s',`monday`='%s',`tuesday`='%s',`wednesday`='%s',`thursday`='%s',`friday`='%s',`saturday`='%s'" + " WHERE `emp_id`=%d",
					Database.getName(), getTableName(), availabilityStrings[0], availabilityStrings[1], availabilityStrings[2], availabilityStrings[3],
					availabilityStrings[4], availabilityStrings[5], availabilityStrings[6], empID));
		}

		// INSERT
		return Database.executeManipulateDataQuery(String.format(
				"INSERT INTO `%s`.`%s` " + "(`emp_id`, `sunday`, `monday`, `tuesday`, `wednesday`, `thursday`, `friday`, `saturday`)"
						+ " VALUES (%d, '%s', '%s', '%s', '%s', '%s', '%s', '%s')",
						Database.getName(), getTableName(), empID, availabilityStrings[0], availabilityStrings[1], availabilityStrings[2], availabilityStrings[3],
						availabilityStrings[4], availabilityStrings[5], availabilityStrings[6]));
	}
	
	/**
	 * Returns a String representation of this Availability's 
	 * availabilityStrings.
	 */
	public String toString() {
		String result = availabilityStrings[0];
		for (int i = 1; i < 7; i++) {
			result = result + "\n" + availabilityStrings[i];
		}
		result.trim();
		return result;
	}
}
