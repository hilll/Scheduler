
public class TimeSlot {
	
	public int day;
	public int start;
	public int end;
	public Employee employee;
	
	public TimeSlot(int d, int start, int end){
		this.day = d;
		this.start=start;
		this.end = end;
		employee = null;
	}
	
	public boolean canFit(TimeSlot that){
		if(this.start <= that.start && this.end >= that.end){
			return true;
		}
		return false;
	}

}
