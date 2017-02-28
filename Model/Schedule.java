import java.util.ArrayList;

public class Schedule {
	
	public ArrayList<TimeSlot>[] timeBlocks;
	public ArrayList<TimeSlot> blockPool;
	
	
	
	public Schedule(){
		
		this.timeBlocks = new ArrayList[7];
		for(int i = 0; i < this.timeBlocks.length ; ++i){
			this.timeBlocks[i] = new ArrayList<TimeSlot>();
		}
		this.blockPool = new ArrayList();

	}
	
	public void AddTimeBlock(TimeSlot slot){
		this.timeBlocks[slot.day].add(slot);
		
	}
	
	public Schedule MakeNewSchedule(Employee[] staff){
		Schedule newSchedule = new Schedule();
		
		
		return newSchedule;	
	}
	
	public void FillBlockPool(Schedule company){
			
		for(int day = 0; day < this.timeBlocks.length; day++){	//time blocks this has
			for(TimeSlot thisSlot : timeBlocks[day] ){
				for(TimeSlot thatSlot : company.timeBlocks[day]){	//time blocks that has
					if(thisSlot.canFit(thatSlot)){
						this.blockPool.add(thatSlot);		//Put shift in emp pool
					}
				}
				
			}
			
			
		}
		
	}

}
