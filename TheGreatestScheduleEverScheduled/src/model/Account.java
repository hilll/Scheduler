package model;

public class Account {
	
	//private Employee employee;
	private Business bus;
	
	public Account() {
		//this.employee = null;
		this.bus = new Business();
	}
	
	public Account(Employee e) {
		//this.employee = e;
		this.bus = new Business();
	}
		
	public Schedule getMasterSchedule(){
		//return employee.business.masterSchedule;
		return bus.getMasterSchedule();
	}
	public Schedule getCurrentSchedule(){
		//return employee.business.currentSchedule;
		return bus.getCurrentSchedule();
	}
	public void createShift(int day, int start, int end, boolean isManager){
		//employee.business.createTimeBlock(day, start, end, empType);
		bus.createTimeBlock(day, start, end, isManager);
	}
	public void deleteShift(int slotID){
		//employee.business.removeTimeBlock(slotID);
		bus.removeTimeBlock(slotID);
	}
	public Schedule createSchedule(){
		//return employee.business.generateNewSchedule();
		return bus.generateNewSchedule();
	}	

}
