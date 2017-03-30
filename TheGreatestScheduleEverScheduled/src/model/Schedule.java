package model;

import java.util.ArrayList;

public class Schedule {
	
	private ArrayList<ArrayList<TimeSlot>> shiftsByDay;
	private ArrayList<TimeSlot> allShiftsPool;
	private Business bus;
	
	public Schedule(Business bus){
		this.bus = bus;
		this.shiftsByDay = new ArrayList<ArrayList<TimeSlot>>(7);
		for(int i = 0; i < 7; ++i){
			shiftsByDay.add(new ArrayList<TimeSlot>());
		}
		this.allShiftsPool = new ArrayList<>();
	}
	
	public void addTimeBlock( int day, int start, int end, boolean isManager, Employee emp) {
		TimeSlot slot = new TimeSlot(bus.getNewTimeSlotID(), day, start, end, isManager);
		slot.setEmployee(emp);
		this.shiftsByDay.get(day).add(slot);
	}
	
	/*
	 * returns true if the TimeBlock with slotID is successfully removed, false if it 
	 * did not exist.
	 */
	public boolean removeTimeBlock(int slotID){
		for(int day = 0; day < 7; day++){
			for(int i = 0 ; i < this.shiftsByDay.get(day).size(); i++){
				if(this.shiftsByDay.get(day).get(i).getID() == slotID){
					this.shiftsByDay.get(day).remove(i);
					return true;
				}
			}
		}
		return false;		
	}
	
	public Schedule makeNewSchedule(Employee[] staff){
		Schedule newSchedule = new Schedule(this.bus);
		return newSchedule;	
	}
	
	public void fillCompanyPool(){
		//empty old pool
		this.allShiftsPool.clear();
		
		//add Time Blocks back in
		for(int day = 0; day < 7; day++){	//time blocks this has
			for(TimeSlot thisSlot : this.shiftsByDay.get(day)){
					this.allShiftsPool.add(thisSlot);
			}	
		}
	}
	
	public void buildTimeBlocksFromPool(){
		this.shiftsByDay = new ArrayList<ArrayList<TimeSlot>>(7);
		for(int i = 0; i < 7; ++i){
			shiftsByDay.add(new ArrayList<TimeSlot>());
		}
		for(TimeSlot block : this.allShiftsPool){
			this.shiftsByDay.get(block.getDay()).add(block);
		}
	}
	
	public void copyBlockPool(Schedule that) {
		for(TimeSlot slot : that.allShiftsPool){
			this.allShiftsPool.add(new TimeSlot(slot.getID(), slot.getDay(), slot.getStart(), slot.getEnd(), slot.getIsManagerTimeSlot()));
		}
	}
	
	public TimeSlot getTimeBlock(TimeSlot slot){
		for(TimeSlot block : this.allShiftsPool){
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
			for(int block = 0; block < this.shiftsByDay.get(i).size(); block++){
				if(firstBlock){
					System.out.print(days[i]+"|");
					
					firstBlock = false;
				}
				else{
					System.out.print("\t|");
				}
				
				boolean inBlock= false;
				for(int c = 0 ; c < 75; c+=3){
					if(this.shiftsByDay.get(i).get(block).getStart() == (c/3)){
						System.out.print("|");
						
						if(this.shiftsByDay.get(i).get(block).getEmployee() != null){
							String name = this.shiftsByDay.get(i).get(block).getEmployee().getFullName() + "----------";
							System.out.print(name.substring(0, 8));
						}
						else
							System.out.printf("id=%2d---", this.shiftsByDay.get(i).get(block).getID());
						c+=8;
						inBlock = true;
					}
					if(this.shiftsByDay.get(i).get(block).getEnd() == (c/3)+1){
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

	public ArrayList<TimeSlot> getAllShiftsPool() {
		return allShiftsPool;
	}
	
	public ArrayList<ArrayList<TimeSlot>> getShiftsByDay() {
		return shiftsByDay;
	}
	
	public Business getBusiness() {
		return this.bus;
	}

}
