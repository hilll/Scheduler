package model;

import java.util.ArrayList;
import java.util.HashMap;

import controller.Database;

public class TimeSlot implements Comparable<TimeSlot> {

	private int day;
	private int start;
	private int end;
	private boolean isManagerTimeSlot;
	private Employee emp;
	private int employeeID;
	private int id;
	private String timeAsString;

	public TimeSlot(int id, String timeAsString, int day, int start, int end, boolean isManagerTimeSlot) {
		this.id = id;
		if (timeAsString != null) { // TimeSlot is being loaded from table
			setStartAndEndFromString(timeAsString);
		} else { // TimeSlot is being created from scratch and needs to be
					// loaded later
			this.start = start;
			this.end = end;
			setTimeAsString();
		}
		this.day = day;
		this.isManagerTimeSlot = isManagerTimeSlot;
		this.id = id;
		this.employeeID = -1;
		if (id != -1)
			this.saveTimeSlot(id);
	}

	public static TimeSlot loadFromID(int id, int empID) {
		String query = "SELECT * FROM " + getTableName() + " WHERE id=" + id;
		ArrayList<HashMap<String, String>> result = Database.executeSelectQuery(query);
		if (result.isEmpty()) {
			System.err.println("Timeslot with id " + id + " does not exist");
			return null;
		}
		HashMap<String, String> hm = result.get(0);
		String manager = hm.get("is_manager");
		boolean isManager = manager.equals("1");
		TimeSlot loaded = new TimeSlot(Integer.parseInt(hm.get("id")), hm.get("time"), Integer.parseInt(hm.get("day")),
				-42, -42, isManager);
		loaded.setEmployeeID(empID);
		return loaded;
	}

	private void setStartAndEndFromString(String timeString) {
		this.timeAsString = timeString;
		int start = -1;
		int end = -1;
		for (int i = 0; i < timeString.length(); i++) {
			if (timeString.charAt(i) == '1') {
				if (start == -1)
					start = i;
				else
					continue;
			} else {
				if (start == -1) // haven't found the start of the shift yet
					continue;
				else
					end = i - 1;
			}
		}
		this.start = start;
		this.end = end;
	}

	private void setTimeAsString() {
		char[] str = new char[96];
		for (int i = 0; i < 96; i++) {
			if (i >= this.start && i <= this.end) {
				str[i] = '1';
			} else {
				str[i] = '0';
			}
		}
		this.timeAsString = String.copyValueOf(str);
	}

	public static String getTableName() {
		return "timeslot";
	}

	public boolean canFit(TimeSlot that) {
		if (this.start <= that.start && this.end >= that.end
				&& this.getIsManagerTimeSlot() == that.getIsManagerTimeSlot()) {
			return true;
		}
		return false;
	}

	public boolean isEqualByID(TimeSlot that) {
		if (this.id == that.id) {
			return true;
		}
		return false;
	}

	public boolean isEqualByDayAndTimes(TimeSlot that) {
		if (this.getDay() == that.getDay() && this.getStart() == that.getStart() && this.getEnd() == that.getEnd())
			return true;
		return false;
	}

	public int getDay() {
		return day;
	}

	public int getStart() {
		return start;
	}

	public int getEnd() {
		return end;
	}

	public boolean getIsManagerTimeSlot() {
		return isManagerTimeSlot;
	}

	public Employee getEmployee() {
		return this.emp;
	}

	public void setEmployee(Employee e) {
		this.emp = e;
	}

	public int getEmployeeID() {
		return employeeID;
	}

	public void setEmployeeID(int empID) {
		this.employeeID = empID;
	}

	public int getID() {
		return id;
	}

	public String getTimeAsString() {
		return this.timeAsString;
	}

	public static int getNewTimeSlotID() {
		return Database.getNextIDForTable(getTableName());
	}

	private String timeToString(int time) {
		String result = "";
		int hours = time / 4;
		if (hours == 0)
			result += "12";
		else if (hours > 12)
			result += hours - 12;
		else
			result += hours;
		result += ":";
		int minutes = time % 4;
		if (minutes == 0)
			result += "00";
		else if (minutes == 1)
			result += "15";
		else if (minutes == 2)
			result += "30";
		else if (minutes == 3)
			result += "45";
		if (hours < 12)
			result += "AM";
		else
			result += "PM";
		return result;
	}

	public static String intToString(int time) {
		String result = "";
		int hours = time / 4;
		if (hours == 0)
			result += "12";
		else if (hours > 12)
			result += hours - 12;
		else
			result += hours;
		result += ":";
		int minutes = time % 4;
		if (minutes == 0)
			result += "00";
		else if (minutes == 1)
			result += "15";
		else if (minutes == 2)
			result += "30";
		else if (minutes == 3)
			result += "45";
		if (hours < 12)
			result += "AM";
		else
			result += "PM";
		return result;
	}

	public String toString() {
		String result = "";
		if (this.day == 0)
			result += "Sunday: ";
		else if (this.day == 1)
			result += "Monday: ";
		else if (this.day == 2)
			result += "Tuesday: ";
		else if (this.day == 3)
			result += "Wednesday: ";
		else if (this.day == 4)
			result += "Thursday: ";
		else if (this.day == 5)
			result += "Friday: ";
		else if (this.day == 6)
			result += "Saturday: ";
		else
			result += "This TimeSlot wasn't formatted correctly.";
		result += timeToString(this.start);
		result += " - ";
		result += timeToString(this.end);
		return result;
	}

	// delete TimeSlot from DB
	public boolean delete(int timeSlotID) {
		return Database.executeManipulateDataQuery(
				String.format("DELETE FROM `%s`.`%s` WHERE `id`='%d'", Database.getName(), getTableName(), timeSlotID));
	}

	public static void deleteTS(int timeSlotID) {
		Database.executeManipulateDataQuery(
				String.format("DELETE FROM `%s`.`%s` WHERE `id`='%d'", Database.getName(), getTableName(), timeSlotID));
	}

	public boolean save(int schedID) {
		boolean result;
		if (Database.masterSchedContainsIDPair(schedID, this.getID())) {
			result = Database.executeManipulateDataQuery(
					String.format("UPDATE `%s`.`%s` SET `emp_id`='%d'" + " WHERE `sched_id`=%d AND `timeslot_id`=%d",
							Database.getName(), Schedule.getMasterScheduleTableName(), this.getEmployeeID(), schedID,
							this.getID()));
		} else {
			// Insert
			result = Database.executeManipulateDataQuery(String.format(
					"INSERT INTO `%s`.`%s` " + "(`sched_id`, `timeslot_id`, `emp_id`)" + " VALUES (%d, %d, %d)",
					Database.getName(), Schedule.getMasterScheduleTableName(), schedID, this.getID(),
					this.getEmployeeID()));
		}
		return result;
	}

	// saves the TimeSlot into the timeslot table, if it is not already there.
	// also will update it if the day, time, or isManager fields change, but
	// this
	// shouldn't really be happening.
	public boolean saveTimeSlot(int timeSlotID) {
		boolean result;
		// UPDATE
		if (Database.tableContainsID(getTableName(), timeSlotID)) {
			result = Database.executeManipulateDataQuery(String.format(
					"UPDATE `%s`.`%s` SET `day`='%s',`time`='%s',`is_manager`=%d" + " WHERE `id`=%d",
					Database.getName(), getTableName(), "" + day, timeAsString, getIsManagerAsInt(), timeSlotID));
		} else {
			// Insert
			result = Database.executeManipulateDataQuery(String.format(
					"INSERT INTO `%s`.`%s` " + "(`id`, `day`, `time`, `is_manager`)" + " VALUES (%d, '%s', '%s', %d)",
					Database.getName(), getTableName(), timeSlotID, "" + day, timeAsString, getIsManagerAsInt()));
		}
		return result;
	}

	private int getIsManagerAsInt() {
		if (this.isManagerTimeSlot)
			return 1;
		else
			return 0;
	}

	@Override
	public int compareTo(TimeSlot o) {
		return this.day - o.getDay();
	}

	@Override
	public boolean equals(Object v) {

		if (v instanceof TimeSlot) {
			TimeSlot other = (TimeSlot) v;
			return other.getDay() == this.getDay() 
					&& other.getStart() == this.getStart() 
					&& other.getEnd() == this.getEnd() 
					&& other.getEmployee().getID() == this.getEmployee().getID()
					&& other.getIsManagerTimeSlot() == this.getIsManagerTimeSlot();
		}

		return false;
	}

}
