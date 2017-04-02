package model;

public class TimeSlot {
	
	private int day;
	private int start;
	private int end;
	private boolean isManagerTimeSlot;
	private Employee employee;
	private int id;
	
	public TimeSlot(int id, int day, int start, int end, boolean isManagerTimeSlot) {
		if (start < 0 || end >= 96) {
			System.out.println("Indexes are out of bounds for start or end. must be 0-95");
		}
		this.day = day;
		this.start=start;
		this.end = end;
		this.isManagerTimeSlot = isManagerTimeSlot;
		this.id =id;
		this.employee = null;
	}
	
	public boolean canFit(TimeSlot that){
		if(this.start <= that.start && this.end >= that.end && this.getIsManagerTimeSlot() == that.getIsManagerTimeSlot()) {
			return true;
		}
		return false;
	}
	
	public boolean isEqualByID(TimeSlot that){
		if(this.id == that.id){
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
		return employee;
	}
	
	public void setEmployee(Employee e) {
		this.employee = e;
	}
	
	public int getID() {
		return id;
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

}
