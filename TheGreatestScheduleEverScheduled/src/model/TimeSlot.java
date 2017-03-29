package model;

public class TimeSlot {
	
	private int day;
	private int start;
	private int end;
	//public String employeeType;
	private boolean isManagerTimeSlot;
	private Employee employee;
	private int id;
	
	public TimeSlot(int id, int day, int start, int end, boolean isManagerTimeSlot){
		this.day = day;
		this.start=start;
		this.end = end;
		this.isManagerTimeSlot = isManagerTimeSlot;
		this.id =id;
		this.employee = null;
	}
	
	/*
	public TimeSlot(int id, int d, int start, int end, String type, Employee emp){
		this.day = d;
		this.start=start;
		this.end = end;
		this.employeeType = type;
		this.id =id;
		this.employee = emp;
	}*/
	
	public boolean canFit(TimeSlot that){
		if(this.start <= that.start && this.end >= that.end && this.getIsManagerTimeSlot() == that.getIsManagerTimeSlot()) {
			return true;
		}
		return false;
	}
	
	public boolean isEqual(TimeSlot that){
		if(this.id == that.id){
			return true;
		}
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

}
