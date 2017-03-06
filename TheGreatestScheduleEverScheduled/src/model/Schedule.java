package model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Random;

public class Schedule implements Serializable{
	
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
	
	public void addTimeBlock( int day, int start, int end, String empType, Employee emp){
		
		TimeSlot slot = new TimeSlot(totalBlocks, day, start, end, empType, emp);
		this.timeBlocks[slot.day].add(slot);
		this.totalBlocks++;
		
	}
	
	public void removeTimeBlock(int slotID){
		for(int day = 0; day < 7; day++){
			for(int i = 0 ; i < this.timeBlocks[day].size(); i++){
				if(this.timeBlocks[day].get(i).id == slotID){
					this.timeBlocks[day].remove(i);
				}
			}
		}
		
	}
	
	public Schedule makeNewSchedule(Employee[] staff){
		Schedule newSchedule = new Schedule();
		
		
		return newSchedule;	
	}
	
	public void fillBlockPool(Schedule company){
		this.blockPool = new ArrayList<TimeSlot>();
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
	public void fillCompanyPool(){
		//empty old pool
		this.blockPool = new ArrayList();
		
		//add Time Blocks back in
		for(int day = 0; day < 7; day++){	//time blocks this has
			for(TimeSlot thisSlot : this.timeBlocks[day] ){
					this.blockPool.add(thisSlot);
			}	
		}
	}
	
	
	
	public void buildTimeBlocksFromPool(){
		this.timeBlocks = new ArrayList[7];
		for(int i = 0; i < this.timeBlocks.length ; ++i){
			this.timeBlocks[i] = new ArrayList<TimeSlot>();
		}
		
		for(TimeSlot block : this.blockPool){
			this.timeBlocks[block.day].add(block);
		}
	}
	
	public void copyBlockPool(Schedule that) {
		for(TimeSlot slot : that.blockPool){
			this.blockPool.add(new TimeSlot(slot.id, slot.day, slot.start, slot.end, slot.employeeType));
		}
	}

	public void setSchedulePrefrences(){
		//Randomize
		long seed = System.nanoTime();
		Collections.shuffle(this.blockPool, new Random(seed));
	}
	
	public TimeSlot getTimeBlock(TimeSlot slot){
		
		for(TimeSlot block : this.blockPool){
			if(block.isEqual(slot)){
				return block;
			}
		}
		return null;
		
		
	}
	
	public void printSchedule(){
		System.out.println("\t| 0  1  2  3  4  5  6  7  8  9 10 11 12 13 14 15 16 17 18 19 20 21 22 23 ");
		String dash = "================================================================================";
		System.out.println( dash);
		String[] days = {"Sun\t","Mon\t", "Tues\t", "Wed\t", "Thur\t", "Fri\t", "Sat\t"};
		for(int i = 0; i < days.length; i++){
			
			//Print day name if first time block, else aline with header
			boolean firstBlock = true;
			for(int block = 0 ;block < this.timeBlocks[i].size(); block++){
				if(firstBlock){
					System.out.print(days[i]+"|");
					
					firstBlock = false;
				}
				else{
					System.out.print("\t|");
				}
				
				boolean inBlock= false;
				for(int c = 0 ; c < 75; c+=3){
					if(this.timeBlocks[i].get(block).start == (c/3)){
						System.out.print("|");
						
						if(this.timeBlocks[i].get(block).employee != null){
							String name = this.timeBlocks[i].get(block).employee.empName + "----------";
							System.out.print(name.substring(0, 8));
						}
						else
							System.out.printf("id=%2d---", this.timeBlocks[i].get(block).id);
						c+=8;
						inBlock = true;
					}
					if(this.timeBlocks[i].get(block).end == (c/3)+1){
						System.out.print("|\n");
						inBlock = false;
						break;
					}
					if(inBlock){
						System.out.print("---");
					}
					else{
						System.out.print("   ");
					}
				}
				
			}
			System.out.println( dash);
		}
		System.out.println();
	}

}
