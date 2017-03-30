package model;

public class Account {
	
	private Business bus;
	
	public Account() {
		this.bus = new Business();
	}
	
	public Schedule getMasterSchedule(){
		return bus.getMasterSchedule();
	}
	
	public Schedule getCurrentSchedule(){
		return bus.getCurrentSchedule();
	}
	
	public void createShift(int day, int start, int end, boolean isManager){
		bus.createTimeBlock(day, start, end, isManager);
	}
	
	public void deleteShift(int slotID){
		bus.removeTimeBlock(slotID);
	}
	
	public Schedule createSchedule(){
		return bus.generateNewSchedule();
	}
	
	public Business getBusiness() {
		return this.bus;
	}
}
