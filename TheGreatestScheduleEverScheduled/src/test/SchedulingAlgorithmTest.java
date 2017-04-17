package test;

import static org.junit.Assert.*;

import java.util.Scanner;

import org.junit.Test;

import model.Business;
import model.Employee;
import model.Schedule;

public class SchedulingAlgorithmTest {
	
	private String[] availWeekdayMornings = {
			"000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000",
			"111111111111111111111111111111111111111111111111111111111111111111000000000000000000000000000000",
			"111111111111111111111111111111111111111111111111111111111111111111000000000000000000000000000000",
			"111111111111111111111111111111111111111111111111111111111111111111000000000000000000000000000000",
			"111111111111111111111111111111111111111111111111111111111111111111000000000000000000000000000000",
			"111111111111111111111111111111111111111111111111111111111111111111000000000000000000000000000000",
			"000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000" };
	private String[] availWeekdayEvenings = {
			"000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000",
			"000000000000000000000000000000000000000000000000000000000011111111111111111111111111111111111111",
			"000000000000000000000000000000000000000000000000000000000011111111111111111111111111111111111111",
			"000000000000000000000000000000000000000000000000000000000011111111111111111111111111111111111111",
			"000000000000000000000000000000000000000000000000000000000011111111111111111111111111111111111111",
			"000000000000000000000000000000000000000000000000000000000011111111111111111111111111111111111111",
			"000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000" };
	private String[] availAllTheTime = {
			"111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111",
			"111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111",
			"111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111",
			"111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111",
			"111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111",
			"111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111",
			"111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111" };
	private String[] availWeekends = {
			"111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111",
			"000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000",
			"000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000",
			"000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000",
			"000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000",
			"000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000",
			"111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111" };

	@Test
	public void test() {
		Business b = new Business(1, "Starbucks");
		Employee katie = new Employee(0, "Katie", "Walters", "katieew@gmail.com", availWeekdayMornings, true);
		Employee james = new Employee(1, "James", "Gauges", "jGauges@gmail.com", availWeekdayEvenings, true);
		Employee cj = new Employee(2, "CJ", "Talksalot", "cjtalk@gmail.com", availWeekends, false);
		Employee ash = new Employee(3, "Ash", "Catchem", "gottacatchemall@gmail.com", availAllTheTime, false);
		Employee paw = new Employee(4, "Paw", "Penguin", "pawbert@gmail.com", availWeekdayEvenings, false);
		Employee filip = new Employee(4, "Filip", "Whitaire", "puffer@gmail.com", availWeekdayMornings, false);
		Employee ms = new Employee(4, "Michael", "Scott", "ms@gmail.com", availAllTheTime, true);
		Employee ds = new Employee(4, "Dwight", "Schrute", "ds@gmail.com", availAllTheTime, false);
		Employee jh = new Employee(4, "Jim", "Halpert", "jh@gmail.com", availAllTheTime, false);
		
		
		katie.setBusiness(b);
		james.setBusiness(b);
		cj.setBusiness(b);
		ash.setBusiness(b);
		paw.setBusiness(b);
		filip.setBusiness(b);
		ms.setBusiness(b);
		ds.setBusiness(b);
		jh.setBusiness(b);
		
		// weekend shifts - two non-managers, all day
		b.createShift(0, 28, 60, false);
		b.createShift(0, 28, 60, false);
		b.createShift(6, 28, 60, false);
		b.createShift(6, 28, 60, false);
		
		//weekday shifts - morning and evening manager with morning and evening employee plus one midday overlap employee
		for (int i = 1; i < 6; i++) {
			b.createShift(i, 24, 62, true);
			b.createShift(i, 24, 62, false);
			b.createShift(i, 62, 95, true);
			b.createShift(i, 62, 95, false);
			b.createShift(i, 44, 76, false);
		}
		
		System.out.println("Before generating anything: \n Master Schedule: \n" + b.getMasterSchedule().toString());
		System.out.println("Current Schedule: \n" + b.getCurrentSchedule().toString());
		
		Schedule newSched = b.generateNewSchedule();
		
		System.out.println("After generating: \n Master Schedule: \n" + b.getMasterSchedule().toString());
		System.out.println("Current Schedule: \n" + b.getCurrentSchedule().toString());
		
		assertEquals(b.getCurrentSchedule().toString(), newSched.toString());
		
		startUserInterface(b);
	}
	
	static Scanner input = new Scanner(System.in);
	private static void startUserInterface(Business busi) {
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
				displaySchedule = busi.getMasterSchedule();
				break;
			case 1: // Display Current Schedule
				displaySchedule = busi.getCurrentSchedule();
				break;
			case 2: // Create Shift, day=0, start=8, end=17, type=worker
				int day = 0; int start = 8; int end = 17; String type = "worker";
				System.out.printf("Creating Shift day=%s start=%d end=%d type=%s\n", day, start, end, type);
				//account.createShift(day, start, end, type);	//creates shift id=21
				displaySchedule = busi.getMasterSchedule();
				
				break;
			case 3: // Delete Shift, id=21
				//account.deleteShift(21);				//removes shift id=21
				displaySchedule = busi.getMasterSchedule();
				break;
			case 4: // Create New Current Schedule
				//account.createSchedule();
				displaySchedule = busi.generateNewSchedule();
				
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

}
