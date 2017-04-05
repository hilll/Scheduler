package test;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.HashMap;

import org.junit.Test;

import controller.Database;
import model.Business;
import model.Employee;
import model.Schedule;

/*
 * TODO fix bug where every saved timeslot in the master_schedule table has an employee_id of 7
 * 
 * TODO test loading up the saved business with its old schedule
 * TODO test adding new shifts and generating a new schedule
 * TODO test saving and make sure both schedules are saved and can be loaded
 * 
 * TODO make sure scheduling "algorithm" doesn't double-book an employee for the same day/shift
 */

public class ScenarioTest {
	
	@Test
	public void testSetUpOfNewBusinessWithEmployeesAndSave() {
		Business bus = new Business(-1, "Scenario Business");
		bus.save(bus.getID());
		Employee tika = new Employee(-1, "Tika", "Bones", "tikaBfluffy@gmail.com", null, false);
		tika.setBusiness(bus);
		tika.save();
		Employee sam = new Employee(-1, "Sam", "Catson", "samuelLcatson@gmail.com", null, false);
		sam.setBusiness(bus);
		sam.save();
		Employee leo = new Employee(-1, "Leonard", "lZar", "lennyLizard@gmail.com", null, true);
		leo.setBusiness(bus);
		leo.save();
		
		assertEquals(bus.getStaff().size(), 3);
		assertEquals("Scenario Business", bus.getName());
		assertTrue(bus.getID() >= 0);
		assertEquals(bus.getID(), tika.getBusiness().getID());
		assertEquals(bus.getID(), sam.getBusiness().getID());
		assertEquals(bus.getID(), leo.getBusiness().getID());
		
		printAllTables();
	}
	
	@Test
	public void loadBusinessAndCheckPreviousSchedules() {
		Business curr = Business.loadFromID(1);
		assertEquals(0, curr.getPrevSchedules().size());
		assertEquals(3, curr.getStaff().size());
		for (int i = 0; i < 3; i++) {
			Employee e = curr.getStaff().get(i);
			assertEquals("", e.getSchedule());
			for (int k = 0; k < 7; k++) {
				assertEquals(1, e.getAvailability().getAvailabilityByDay().get(k).size());
			}
		}
		for (int j = 0; j < 7; j++) {
			curr.createShift(j, 32, 68, false);
			curr.createShift(j, 32, 68, true);
		}
		assertEquals(14, curr.getMasterSchedule().getAllShiftsPool().size());
		Schedule newSched = curr.generateNewSchedule();
		assertTrue(curr.saveCurrentSchedule());
		System.out.println(newSched.toString());
		curr.save(curr.getID());
	}
	
	@Test
	public void loadBusinessAndAddANewSchedule() {
		Business curr = Business.loadFromID(1);
		assertEquals(1, curr.getPrevSchedules().size());
		assertEquals(3, curr.getStaff().size());
		System.out.println("Printing shifts:");
		for (String shift : curr.getMasterSchedule().getAllShiftsAsStrings()) {
			System.out.println(shift);
		}
		assertEquals(14, curr.getMasterSchedule().getAllShiftsPool().size());
		Schedule newSched = curr.getCurrentSchedule();
		assertTrue(curr.saveCurrentSchedule());
		curr.save(curr.getID());
	}
	
	@Test
	public void deleteEverything() {
		Schedule.delete(0);
	}
	
	@Test
	public void printing() {
		printAllTables();
	}
	
	public void printAllTables() {
		System.out.println("Printing business table:");
		ArrayList<HashMap<String, String>> businessTableData = Database.executeSelectQuery("SELECT * FROM business");
		Database.printData(businessTableData);
		System.out.println();
		
		System.out.println("Printing schedule table:");
		ArrayList<HashMap<String, String>> scheduleTableData = Database.executeSelectQuery("SELECT * FROM schedule");
		Database.printData(scheduleTableData);
		System.out.println();
		
		System.out.println("Printing master_schedule table:");
		ArrayList<HashMap<String, String>> masterScheduleTableData = Database.executeSelectQuery("SELECT * FROM master_schedule");
		Database.printData(masterScheduleTableData);
		System.out.println();
		
		System.out.println("Printing timeslot table:");
		ArrayList<HashMap<String, String>> timeslotTableData = Database.executeSelectQuery("SELECT * FROM timeslot");
		Database.printData(timeslotTableData);
		System.out.println();
		
		System.out.println("Printing employee table:");
		ArrayList<HashMap<String, String>> employeeTableData = Database.executeSelectQuery("SELECT * FROM employee");
		Database.printData(employeeTableData);
		System.out.println();
		
		System.out.println("Printing availability table:");
		ArrayList<HashMap<String, String>> availabilityTableData = Database.executeSelectQuery("SELECT * FROM availability");
		Database.printData(availabilityTableData);
		System.out.println();
	}
	
	@Test
	public void createADummyEmployeeToOwnOwnerlessTimeSlots() {
		Employee emp = new Employee(7, "Unassigned", "TimeSlotOwner", "dummyaccount@gmail.com", null, false);
		Business bus = Business.loadFromID(0);
		emp.setBusiness(bus);
		emp.save();
	}

}
