import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Random;

public class Schedule {
	
	public ArrayList<TimeSlot>[] timeBlocks;
	public ArrayList<TimeSlot> blockPool;
	public int totalBlocks;
	
	
	
	public Schedule(){
		
		this.timeBlocks = new ArrayList[7];
		for(int i = 0; i < this.timeBlocks.length ; ++i){
			this.timeBlocks[i] = new ArrayList<TimeSlot>();
		}
		this.blockPool = new ArrayList();
		this.totalBlocks=0;

	}
	
	public void addTimeBlock(TimeSlot slot){
		this.timeBlocks[slot.day].add(slot);
		this.totalBlocks++;
		
	}
	
	public Schedule makeNewSchedule(Employee[] staff){
		Schedule newSchedule = new Schedule();
		
		
		return newSchedule;	
	}
	
	public void fillBlockPool(Schedule company){
			
		for(int day = 0; day < this.timeBlocks.length; day++){	//time blocks this has
			for(TimeSlot thisSlot : this.timeBlocks[day] ){
				for(TimeSlot thatSlot : company.timeBlocks[day]){	//time blocks that has
					if(thisSlot.canFit(thatSlot)){
						this.blockPool.add(thatSlot);		//Put shift in emp pool
					}
				}
				
			}
			
		}
		
	}
	
	public Schedule generateNewSchedule(Schedule compSchedule, Employee[] staff) throws CloneNotSupportedException{
		
		//prepare new schedule
		Schedule newSchedule = (Schedule) compSchedule.clone();
		
		
		for(int numBlock = 0; numBlock < compSchedule.totalBlocks; numBlock++ ){		//START TMA
			for(Employee emp : staff){
				emp.empAvailability.blockPool.get(numBlock);
			}
		}
		
		return newSchedule;
	}
	
	public void setSchedulePrefrences(){
		//Randomize
		Collections.shuffle(this.blockPool, new Random(this.blockPool.size()));
	}

}
