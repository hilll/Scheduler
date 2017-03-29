package model;

public class Account {
	
	public Employee employee;
	
	public Account() {
		this.employee = null;
	}
	
	public Account(Employee e) {
		this.employee = e;
	}
		
//	public Schedule getMasterSchedule(){
//		return employee.business.masterSchedule;
//	}
//	public Schedule getCurrentSchedule(){
//		return employee.business.currentSchedule;
//	}
//	public void createShift(int day, int start, int end, String empType){
//		employee.business.createTimeBlock(day, start, end, empType);
//		
//	}
//	public void deleteShift(int slotID){
//		employee.business.removeTimeBlock(slotID);
//		
//	}
//	public Schedule createSchedule(){
//		return employee.business.generateNewSchedule();
//	}

	public Employee getEmployee() {
		return employee;
	}
	

}
