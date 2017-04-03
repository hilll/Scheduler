package test;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.HashMap;

import org.junit.Test;

import controller.Database;
import model.Business;

public class BusinessTest {

	public void printRelatedTables() {
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
	}
	
	@Test
	public void testCreateSaveAndLoad() {
		Business bus = new Business(-1, "Tester Business");
		assertEquals("Tester Business", bus.getName());
		
		/*
		 * Constructor should have grabbed the next available
		 * business_id, so id should not equal -1.
		 */
		assertFalse(bus.getID() == -1);
		System.out.println("bus.getID() = " + bus.getID());
		
		assertEquals(0, bus.getCurrentSchedule().getAllShiftsPool().size());
		assertEquals(0, bus.getMasterSchedule().getAllShiftsPool().size());
		assertEquals(0, bus.getStaff().size());
		
		System.out.println("Before save:\n");
		
		printRelatedTables();
		
		bus.save(bus.getID());
		
		System.out.println("After save:\n");
		
		printRelatedTables();
		
		System.out.println("Loading business");
		bus = Business.loadFromID(bus.getID());
		
		assertEquals(0, bus.getCurrentSchedule().getAllShiftsPool().size());
		assertEquals(0, bus.getMasterSchedule().getAllShiftsPool().size());
		assertEquals(0, bus.getStaff().size());
		assertEquals("Tester Business", bus.getName());
		bus.setName("Changed Name");
		assertEquals("Changed Name", bus.getName());
		System.out.println("******************bus.getID() == " + bus.getID());
		bus.save(bus.getID());
		
		System.out.println("After save:\n");
		
		printRelatedTables();
		
		System.out.println("Loading business");
		bus = Business.loadFromID(bus.getID());
		
		assertEquals("Changed Name", bus.getName());
		assertEquals(0, bus.getCurrentSchedule().getAllShiftsPool().size());
		assertEquals(0, bus.getMasterSchedule().getAllShiftsPool().size());
		assertEquals(0, bus.getStaff().size());
		
		bus.delete();
		
		System.out.println("After delete:\n");
		
		printRelatedTables();
		
		System.out.println("Deleting extras:");
		
		loadAndDeleteExtras();
		
		System.out.println("After deletion of extras:");
		printRelatedTables();
	}
	
	public void loadAndDeleteExtras() {
		int i = 1;
		Business deleteMe = Business.loadFromID(i);
		while (deleteMe != null) {
			deleteMe.delete();
			i++;
			deleteMe = Business.loadFromID(i);
		}
	}

}
