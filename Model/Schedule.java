import java.util.ArrayList;

public class Schedule {
	
	public ArrayList<TimeSlot>[] timeBlocks;
	public ArrayList<TimeSlot> blockPool;
	
	
	
	public Schedule(){
		
		this.timeBlocks = new ArrayList[7];
		this.blockPool = new ArrayList();

	}
	
	public void AddTimeBlock(TimeSlot slot){
		this.timeBlocks[slot.day].add(slot);
		
	}
	
	public Schedule MakeNewSchedule(Employee[] staff){
		Schedule newSchedule = new Schedule();
		
		
		return newSchedule;
		
		
	}
	

}
