package model;

import java.io.Serializable;
import java.util.ArrayList;

public class Business implements Serializable{
	
	public Schedule masterSchedule;
	public Schedule currentSchedule;
	public ArrayList<Schedule> previousSchedules;
	public Employee[] employees;
	
	public Business(){
		this.masterSchedule = new Schedule();
		this.currentSchedule = new Schedule();
		this.previousSchedules = new ArrayList<Schedule>();
		this.employees = new Employee[10];
		 
	}
	
	public void setBusinessMasterSchedule(Schedule schedule){
		this.masterSchedule=schedule;
	}

}
