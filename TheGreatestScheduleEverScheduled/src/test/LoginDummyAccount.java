package test;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Scanner;

import model.*;

public class LoginDummyAccount {
	
	static Scanner input = new Scanner(System.in);
	
	public static void main(String args[]){
		
		//Saves a new dummy account, this is not used by WUI (Web User Interface)
		//saveNewDummyAccount();
		
		Account account = new Account();
		
		startUserInterface(account);
	}

	private static void startUserInterface(Account account) {
		Schedule displaySchedule = null;
		System.out.println("Welcome the The Greatest Schedule Ever Scheduled");
		System.out.println("=================================================");
		System.out.println("Concepts, there are two different schedules.....");
		System.out.println("\tMaster Schedule = The business's schedule of shifts in needs to fill with the shift id displayed");
		System.out.println("\tCurrent Schedule = The currently generated schedule for the week");
		System.out.println("");
		System.out.println("Actions");
		System.out.println("\tDisplay = Shows that edition of the schdule");
		System.out.println("\tCreate Shift = Will add a predetermined shift to the Master schedule (it will not appear until a new current schedule is created)");
		System.out.println("\tDelete Shift = Will delete the pretermined shift");
		System.out.println("\tCreate Schedule = Generate a new current schedule");
		System.out.println("");
		while(true){
			
			
			System.out.println("\n\nOptions\n--------------");
			String[] options = {"Display Master Schedule",
								"Display Current Schedule",
								"Create Shift",
								"Delete Shift",
								"Create New Current Schedule",
								"Quit"};
			int choice;
			for(int i = 0; i < options.length; i++){
				System.out.println(i + ") " + options[i]);
			}
			//get user input
			System.out.print("Please choose option > ");
			choice = input.nextInt();
			input.nextLine();
			
			switch(choice){
			case 0: //Display Master Schedule
				displaySchedule = account.getMasterSchedule();
				break;
			case 1: // Display Current Schedule
				displaySchedule = account.getCurrentSchedule();
				break;
			case 2: // Create Shift, day=0, start=8, end=17, type=worker
				int day = 0; int start = 8; int end = 17; boolean isManager = false;
				System.out.printf("Creating Shift day=%s start=%d end=%d type=%b\n", day, start, end, isManager);
				account.createShift(day, start, end, isManager);	//creates shift id=21
				displaySchedule = account.getMasterSchedule();
				
				break;
			case 3: // Delete Shift, id=21
				account.deleteShift(21);				//removes shift id=21
				displaySchedule = account.getMasterSchedule();
				break;
			case 4: // Create New Current Schedule
				account.createSchedule();
				displaySchedule = account.getCurrentSchedule();
				
				break;
			case 5: // Quit
				System.out.println("Quiting.... All changes WILL NOT be saved");
				return;
			default:
				System.out.println("Choice Not Found, Please try again");
				break;
			}
			
			//Print current display Schedule
			displaySchedule.printSchedule();
		}
		
	}


	private static void saveNewDummyAccount() {
		Account account = new Account();
		Business business = new Business();
		
		// DUmmy Company Scedule, 2 10 hour shifts a day, 7 day of the week
		Schedule businessSchedule = new Schedule();
		for(int i = 0; i < 7; i++){
			businessSchedule.addTimeBlock(i, 0, 10, false, null);
			businessSchedule.addTimeBlock(i, 10, 20, false, null);
			businessSchedule.addTimeBlock(i, 0, 20, false, null);
		}
		
		
		//Fill Company Pool with Shifts
		businessSchedule.fillCompanyPool();
		
		//Add master schedule
		business.setMasterSchedule(businessSchedule);
		
		
		// Intialize Dummy Employee workers
		ArrayList<Employee> staff = new ArrayList<Employee>();
		int i;
		for( i = 0; i < 10; ++i){
			// Assign open availability all day
			Schedule empSchedule = new Schedule();
			String fname = "worker" + i ;
			String lname = "Bee";
			//staff[i] = new Employee(i, name, 40, empSchedule);
			Employee e = new Employee(i, fname, lname, null, false);
			staff.add(e);
			for(int j = 0; j < 7; j++){
				empSchedule.addTimeBlock(j, 0, 20, false, e);
			}

			
			
		}
		// Intialize Dummy Employee workers
		for( int k = 0; k < 12; ++k){
			// Assign open availability all day
			Schedule empSchedule = new Schedule();
			String fname = "manager" + k;
			String lname = "Bee";
			Employee e = new Employee(i, fname, lname, null, true);
			//staff[i] = new Employee(i, name, 40, empSchedule);
			
			for(int j = 0; j < 7; j++){
				empSchedule.addTimeBlock(j, 0, 20, true, e);
			}
			//name increament
			k++;
			
		}
		
		//Finish initializing dummy business
		business.setStaff(staff);
		business.generateNewSchedule();
		
		//Initialize account
		//staff[10].business = business;
		//account.employee = staff[10];
		
		//account.SaveAccount(account);
		
		
	}

}
