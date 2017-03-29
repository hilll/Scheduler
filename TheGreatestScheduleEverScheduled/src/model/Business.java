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
		this.setMasterSchedule(new Schedule());
		this.currentSchedule = new Schedule();
		this.previousSchedules = new ArrayList<Schedule>();
		// Changed this to allow for business growth: this.staff = new Employee[10];
		this.staff = new ArrayList<Employee>();
	}
	
	public void setBusinessMasterSchedule(Schedule schedule){
		this.setMasterSchedule(schedule);
	}
	
	public Schedule generateNewSchedule() {
		
		//Intitalize new schedule with company block Pool, which is neededShifts?????????
		Schedule newSchedule = new Schedule();
		newSchedule.copyBlockPool(getMasterSchedule());
		
		//Initialize employee block pools
		initializeStaffPools();
		
		
		int numBlocks = getMasterSchedule().getAllShiftsPool().size();
		int numRounds = 0;
		while(numBlocks > 0 && numRounds < (getMasterSchedule().getAllShiftsPool().size()-10) ){		//START TMA
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
			emp.getAvailability().fillBlockPool(getMasterSchedule());
			
			//Set preferences - iteration 1 = RANDOMIZE
			emp.getAvailability().setSchedulePrefrences();
		}
	}

	public void createTimeBlock(int day, int start, int end, boolean isManager){
		getMasterSchedule().addTimeBlock(day, start, end, isManager, null);
		getMasterSchedule().fillCompanyPool();
	}

	public void removeTimeBlock(int slotID) {
		getMasterSchedule().removeTimeBlock(slotID);
		getMasterSchedule().fillCompanyPool();
	}
	
	public void addEmployee(Employee emp) {
		staff.add(emp);
	}
	
	/*
	 * removes employee & returns true if it exists in the business's staff,
	 * else returns false
	 */
	public boolean removeEmployee(Employee emp) {
		for (Employee e : staff) {
			if (e.equals(emp)) {
				staff.remove(e);
				return true;
			}
		}
		return false;
	}
	
	public Schedule getMasterSchedule() {
		return masterSchedule;
	}
	
	public Schedule getCurrentSchedule() {
		return currentSchedule;
	}

	public void setMasterSchedule(Schedule masterSchedule) {
		this.masterSchedule = masterSchedule;
	}
	
	public void setStaff(ArrayList<Employee> staff) {
		this.staff = staff;
	}

}
