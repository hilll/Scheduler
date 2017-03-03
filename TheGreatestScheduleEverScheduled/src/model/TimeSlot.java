package model;

public class TimeSlot {
	
	public int day;
	public int start;
	public int end;
	public String employeeType;
	public Employee employee;
	private int id;
	
	public TimeSlot(int id, int d, int start, int end, String type){
		this.day = d;
		this.start=start;
		this.end = end;
		this.employeeType = type;
		this.id =id;
		this.employee = null;
	}
	
	public boolean canFit(TimeSlot that){
		if(this.start <= that.start && this.end >= that.end && this.employeeType.equals(that.employeeType)){
			return true;
		}
		return false;
	}
	
	public boolean isEqual(TimeSlot that){
		
//		if(this.employeeType.equals(that.employeeType) && this.day == that.day &&
//				this.start == that.start && this.end == that.end && this.employee.equals(that.employee)){
		if(this.id == that.id){
			return true;
		}
		return false;
	}

}
