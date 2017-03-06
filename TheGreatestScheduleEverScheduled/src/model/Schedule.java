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
	
	public void addTimeBlock( int day, int start, int end, String empType){
		
		TimeSlot slot = new TimeSlot(totalBlocks, day, start, end, empType);
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
	public void fillCompanyPool(){
		
		for(int day = 0; day < 7; day++){	//time blocks this has
			for(TimeSlot thisSlot : this.timeBlocks[day] ){
					this.blockPool.add(thisSlot);
			}	
		}
	}
	
	public Schedule generateNewSchedule(Schedule compSchedule, Employee[] staff) {
		
		//Intitalize new schedule with company block Pool
		Schedule newSchedule = new Schedule();
		newSchedule.copyBlockPool(compSchedule);
		
		
		int numBlocks = compSchedule.totalBlocks;
		int numRounds = 0;
		while(numBlocks > 0 && numRounds < (compSchedule.blockPool.size()-10) ){		//START TMA
			for(Employee emp : staff){
				if(numRounds < emp.empAvailability.blockPool.size()){
					TimeSlot empChoice = emp.empAvailability.blockPool.get(numRounds);
					TimeSlot shift = compSchedule.getTimeBlock(empChoice);
					if(shift.employee == null){
						shift.employee = emp;
						numBlocks--;
					}
				}
				
			}
			numRounds++;
		}
		if(numBlocks != 0){
			System.out.println("SOME SHIFTS LEFT OPEN");
		}
		
		//Fill TimeBlock
		newSchedule.buildTimeBlocksFromPool();
		
		return newSchedule;
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
	
	private void copyBlockPool(Schedule that) {
		for(TimeSlot slot : that.blockPool){
			this.blockPool.add(slot);
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
		System.out.println("\n\n\t| 0  1  2  3  4  5  6  7  8  9 10 11 12 13 14 15 16 17 18 19 20 21 22 23 ");
		String dash = "================================================================================";
		System.out.println( dash);
		String[] days = {"Sun\t","Mon\t", "Tues\t", "Wed\t", "Thur\t", "Fri\t", "Sat\t"};
		for(int i = 0; i < days.length; i++){
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
							System.out.print("null----");
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
	}

}
