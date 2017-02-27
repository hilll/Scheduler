import java.util.ArrayList;

public class Schedule {
	
	ArrayList<TimeSlot>[] timeBlocks;
	
	
	public Schedule(){
		
		timeBlocks = new ArrayList[7];

	}
	
	public void AddTimeSlot(TimeSlot slot){
		timeBlocks[slot.day].add(slot);
		
	}
	
	public Schedule MakeNewSchedule(){
		Schedule newSchedule = new Schedule();
		
		
		return newSchedule;
		
		
	}
	

}
