
public class TimeSlot {
	
	public int day;
	public int start;
	public int end;
	public String employeeType;
	public Employee employee;
	
	public TimeSlot(int d, int start, int end, String type){
		this.day = d;
		this.start=start;
		this.end = end;
		this.employeeType = type;
		employee = null;
	}
	
	public boolean canFit(TimeSlot that){
		if(this.start <= that.start && this.end >= that.end){
			return true;
		}
		return false;
	}
	
	public boolean isEqual(TimeSlot that){
		if(this.employeeType.equals(that.employeeType) && this.day == that.day &&
				this.start == that.start && this.end == that.end){
			return true;
		}
		return false;
	}

}
