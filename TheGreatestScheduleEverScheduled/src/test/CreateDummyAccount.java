package test;

import model.*;

public class CreateDummyAccount {
	
	public static void main(String args[]){
		
		saveNewDummyAccount();
		
		Account account = new Account();
		account.LoadAccount();
		displayDummyAccount(account);
		
		
	}

	private static void displayDummyAccount(Account account) {
		System.out.println("Employee Name: " + account.employee.empName);
		System.out.println("Employee Availability Schedule");
		account.employee.empAvailability.printSchedule();
		System.out.println("Business Master Schedule");
		account.employee.business.masterSchedule.printSchedule();
		System.out.println("\nBusiness Current Schedule");
		account.employee.business.currentSchedule.printSchedule();
		
	}

	private static void saveNewDummyAccount() {
		Account account = new Account();
		Business business = new Business();
		
		/* DUmmy Company Scedule, 2 10 hour shifts a day, 7 day of the week */
		Schedule businessSchedule = new Schedule();
		for(int i = 0; i < 7; i++){
			businessSchedule.addTimeBlock(i, 0, 10, "worker", null);
			businessSchedule.addTimeBlock(i, 10, 20, "worker", null);
			businessSchedule.addTimeBlock(i, 0, 20, "manager", null);
		}
		
		
		//Fill Company Pool with Shifts
		businessSchedule.fillCompanyPool();
		
		//Add master schedule
		business.masterSchedule=businessSchedule;
		
		
		/* Intialize Dummy Employee workers*/
		Employee[]	staff = new Employee[12];
		int i;
		for( i = 0; i < 10; ++i){
			// Assign open availability all day
			Schedule empSchedule = new Schedule();
			String name = "worker" + i ;
			staff[i] = new Employee(i, name, 40, empSchedule);
			for(int j = 0; j < 7; j++){
				empSchedule.addTimeBlock(j, 0, 20, "worker", staff[i]);
			}
			
			
			//Fill employee shiftpool
			empSchedule.fillBlockPool(businessSchedule);
			
			//Set prefrences - iteration 1 = RANDOMIZE
			empSchedule.setSchedulePrefrences();
			
			
		}
		/* Intialize Dummy Employee workers*/
		for( int k = 0; i < 12; ++i){
			// Assign open availability all day
			Schedule empSchedule = new Schedule();
			String name = "manager" + k ;
			
			staff[i] = new Employee(i, name, 40, empSchedule);
			for(int j = 0; j < 7; j++){
				empSchedule.addTimeBlock(j, 0, 20, "manager", staff[i]);
			}
			
			//Fill employee shiftpool
			empSchedule.fillBlockPool(businessSchedule);
			
			//Set prefrences - iteration 1 = RANDOMIZE
			empSchedule.setSchedulePrefrences();
			
			k++;
			
		}
		
		//Finish initializing dummy business
		business.employees = staff;
		business.currentSchedule = businessSchedule.generateNewSchedule(businessSchedule, staff);
		
		//Initialize account
		staff[10].business = business;
		account.employee = staff[10];
		
		account.SaveAccount(account);
		
		
	}
	

}
