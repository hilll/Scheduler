package model;

import java.io.Serializable;
import java.util.ArrayList;

public class Business implements Serializable{
	
	public Schedule masterSchedule;
	public Schedule currentSchedule;
	public ArrayList<Schedule> previousSchedules;
	public Employee[] staff;
	
	public Business(){
		this.masterSchedule = new Schedule();
		this.currentSchedule = new Schedule();
		this.previousSchedules = new ArrayList<Schedule>();
		this.staff = new Employee[10];
		 
	}
	
	public void setBusinessMasterSchedule(Schedule schedule){
		this.masterSchedule=schedule;
	}
	
	public Schedule generateNewSchedule() {
		
		//Intitalize new schedule with company block Pool
		Schedule newSchedule = new Schedule();
		newSchedule.copyBlockPool(masterSchedule);
		
		//Initialize employee block pools
		initializeStaffPools();
		
		
		int numBlocks = masterSchedule.blockPool.size();
		int numRounds = 0;
		while(numBlocks > 0 && numRounds < (masterSchedule.blockPool.size()-10) ){		//START TMA
			for(Employee emp : staff){
				if(numRounds < emp.empAvailability.blockPool.size()){
					TimeSlot empChoice = emp.empAvailability.blockPool.get(numRounds);
					TimeSlot shift = newSchedule.getTimeBlock(empChoice);
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
		
		//Make it the new current schedule
		this.currentSchedule = newSchedule;
		
		return newSchedule;
	}
	
	private void initializeStaffPools() {
		for(Employee emp : staff){
			//Fill employee shiftpool
			emp.empAvailability.fillBlockPool(masterSchedule);
			
			//Set prefrences - iteration 1 = RANDOMIZE
			emp.empAvailability.setSchedulePrefrences();
		}
		
	}

	public void createTimeBlock(int day, int start, int end, String empType){
		masterSchedule.addTimeBlock(day, start, end, empType, null);
		masterSchedule.fillCompanyPool();

	}

	public void removeTimeBlock(int slotID) {
		masterSchedule.removeTimeBlock(slotID);
		masterSchedule.fillCompanyPool();
		
	}

}
