package model;
import java.util.ArrayList;

public class Business {
	
	private final int MAX_EMP_HOURS = 100; // (25 hours per week * 4 time blocks per hour)
	private final int MAX_MANAGER_HOURS = 160; // (40 hours per week * 4 time blocks per hour)
	private Schedule masterSchedule;
	private Schedule currentSchedule;
	private ArrayList<Schedule> previousSchedules;
	private ArrayList<Employee> staff;
	
	public Business(){
		this.masterSchedule = new Schedule();
		this.currentSchedule = new Schedule();
		this.previousSchedules = new ArrayList<Schedule>();
		// Changed this to allow for business growth: this.staff = new Employee[10];
		this.staff = new ArrayList<Employee>();
	}
	
	public void setBusinessMasterSchedule(Schedule schedule){
		this.masterSchedule=schedule;
	}
	
	public Schedule generateNewSchedule() {
		
		//Intitalize new schedule with company block Pool, which is neededShifts?????????
		Schedule newSchedule = new Schedule();
		newSchedule.copyBlockPool(masterSchedule);
		
		//Initialize employee block pools
		initializeStaffPools();
		
		
		int numBlocks = masterSchedule.getAllShiftsPool().size();
		int numRounds = 0;
		while(numBlocks > 0 && numRounds < (masterSchedule.getAllShiftsPool().size()-10) ){		//START TMA
			for(Employee emp : staff){
				if(numRounds < emp.getAvailability().getAvailabilityPool().size()){
					TimeSlot empChoice = emp.getAvailability().getAvailabilityPool().get(numRounds);
					TimeSlot shift = newSchedule.getTimeBlock(empChoice);
					if(shift.getEmployee() == null){
						shift.setEmployee(emp);
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
	
	/*
	 * Adds all of the availability blocks of each employee to the masterSchedule's blockPool
	 */
	private void initializeStaffPools() {
		for(Employee emp : staff){
			//Fill employee shiftpool
			emp.getAvailability().fillBlockPool(masterSchedule);
			
			//Set preferences - iteration 1 = RANDOMIZE
			emp.getAvailability().setSchedulePrefrences();
		}
	}

	public void createTimeBlock(int day, int start, int end, boolean isManager){
		masterSchedule.addTimeBlock(day, start, end, isManager, null);
		masterSchedule.fillCompanyPool();
	}

	public void removeTimeBlock(int slotID) {
		masterSchedule.removeTimeBlock(slotID);
		masterSchedule.fillCompanyPool();
	}

}
