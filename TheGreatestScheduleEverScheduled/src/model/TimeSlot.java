package model;

import java.io.Serializable;

public class TimeSlot implements Serializable {
	
	public int day;
	public int start;
	public int end;
	public String employeeType;
	public Employee employee;
	public int id;
	
	public TimeSlot(int id, int d, int start, int end, String type){
		this.day = d;
		this.start=start;
		this.end = end;
		this.employeeType = type;
		this.id =id;
		this.employee = null;
	}
	public TimeSlot(int id, int d, int start, int end, String type, Employee emp){
		this.day = d;
		this.start=start;
		this.end = end;
		this.employeeType = type;
		this.id =id;
		this.employee = emp;
	}
	
	public boolean canFit(TimeSlot that){
		if(this.start <= that.start && this.end >= that.end && this.employeeType.equals(that.employeeType)){
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

}
