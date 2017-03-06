package test;

import java.sql.SQLException;
import java.util.Scanner;

import model.*;

public class LoginDummyAccount {
	
	static Scanner input = new Scanner(System.in);
	
	public static void main(String args[]){
		
		saveNewDummyAccount();
		
		Account account = new Account();
		account.LoadAccount();
		
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
				int day = 0; int start = 8; int end = 17; String type = "worker";
				System.out.printf("Creating Shift day=%s start=%d end=%d type=%s\n", day, start, end, type);
				account.createShift(day, start, end, type);	//creates shift id=21
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
			//name increament
			k++;
			
		}
		
		//Finish initializing dummy business
		business.staff = staff;
		business.generateNewSchedule();
		
		//Initialize account
		staff[10].business = business;
		account.employee = staff[10];
		
		account.SaveAccount(account);
		
		
	}
	

}
