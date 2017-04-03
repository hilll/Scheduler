package test;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.HashMap;

import org.junit.Test;

import controller.Database;
import model.TimeSlot;

public class TimeSlotTest {

	@Test
	public void test() {
		TimeSlot ts = new TimeSlot(-1, null, 0, 0, 15, true);
		assertEquals(0, ts.getDay());
		assertEquals(0, ts.getStart());
		assertEquals(15, ts.getEnd());
		assertTrue(ts.getIsManagerTimeSlot());
		assertEquals(0, ts.getID());
		assertEquals("Sunday: 12:00AM - 3:45AM", ts.toString());
		
		TimeSlot ts2 = new TimeSlot(-1, null, 0, 0, 15, true);
		assertTrue(ts.canFit(ts2));
		assertTrue(ts2.canFit(ts));
		
		TimeSlot ts3 = new TimeSlot(-1, null, 0, 0, 21, true);
		assertTrue(ts3.canFit(ts2));
		assertFalse(ts2.canFit(ts3));
		
		TimeSlot ts4 = new TimeSlot(-1, null, 2, 57, 95, true);
		assertEquals(2, ts4.getDay());
		assertEquals(57, ts4.getStart());
		assertEquals(95, ts4.getEnd());
		assertTrue(ts4.getIsManagerTimeSlot());
		assertEquals("Tuesday: 2:15PM - 11:45PM", ts4.toString());
		
		TimeSlot ts5 = new TimeSlot(-1, null, 1, 56, 95, true);
		assertEquals(1, ts5.getDay());
		assertEquals(56, ts5.getStart());
		assertEquals(95, ts5.getEnd());
		assertTrue(ts5.getIsManagerTimeSlot());
		assertEquals("Monday: 2:00PM - 11:45PM", ts5.toString());
		
		TimeSlot ts6 = new TimeSlot(-1, null, 3, 55, 94, true);
		assertEquals("Wednesday: 1:45PM - 11:30PM", ts6.toString());
		
		TimeSlot ts7 = new TimeSlot(-1, null, 4, 54, 93, true);
		assertEquals("Thursday: 1:30PM - 11:15PM", ts7.toString());
		
		TimeSlot ts8 = new TimeSlot(-1, null, 5, 48, 92, true);
		assertEquals("Friday: 12:00PM - 11:00PM", ts8.toString());
		
		TimeSlot ts9 = new TimeSlot(-1, null, 6, 49, 91, true);
		assertEquals("Saturday: 12:15PM - 10:45PM", ts9.toString());
		
		TimeSlot ts10 = new TimeSlot(-1, null, 6, 47, 90, true);
		assertEquals("Saturday: 11:45AM - 10:30PM", ts10.toString());

		TimeSlot ts11 = new TimeSlot(-1, null, 0, 1, 15, true);
		assertEquals("Sunday: 12:15AM - 3:45AM", ts11.toString());
		
		TimeSlot ts12 = new TimeSlot(-1, null, 0, 47, 49, true);
		assertEquals("Sunday: 11:45AM - 12:15PM", ts12.toString());
	}
	
	// id, String timeAsString, int day, int start, int end, boolean isManagerTimeSlot) {

	public String starter = "111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111";
	public String otherrr = "000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000";
	
	@Test
	public void testInsertUpdateDelete() {
		TimeSlot first = new TimeSlot(TimeSlot.getNewTimeSlotID(), null, 0, 0, 95, false);
		assertEquals(starter, first.getTimeAsString());
		assertEquals(0, first.getDay());
		assertEquals(0, first.getStart());
		assertEquals(95, first.getEnd());
		assertFalse(first.getIsManagerTimeSlot());
		assertEquals("Sunday: 12:00AM - 11:45PM", first.toString());
		
		System.out.println("Printing TimeSlot table before save:");
		ArrayList<HashMap<String, String>> data = Database.executeSelectQuery("SELECT * FROM timeslot");
		Database.printData(data);
		System.out.println();
		
		first.saveTimeSlot(first.getID());
		
		System.out.println("Printing TimeSlot table after save:");
		data = Database.executeSelectQuery("SELECT * FROM timeslot");
		Database.printData(data);
		System.out.println();
		
		first.delete(first.getID());
		
		System.out.println("Printing TimeSlot table after delete:");
		data = Database.executeSelectQuery("SELECT * FROM timeslot");
		Database.printData(data);
		System.out.println();
		
		System.out.println("Recreating first TimeSlot to test update");
		first = new TimeSlot(TimeSlot.getNewTimeSlotID(), null, 0, 0, 95, false);
        first.saveTimeSlot(first.getID());
		
		System.out.println("Printing TimeSlot table after recreation and save:");
		data = Database.executeSelectQuery("SELECT * FROM timeslot");
		Database.printData(data);
		System.out.println();
		
		first = new TimeSlot(first.getID(), otherrr, 1, -1, -1, true);
		first.saveTimeSlot(first.getID());
		
		System.out.println("Printing TimeSlot table after updating the time for first and saving.");
		data = Database.executeSelectQuery("SELECT * FROM timeslot");
		Database.printData(data);
		System.out.println();
		
		first.delete(first.getID());
		System.out.println("Printing TimeSlot table after delete:");
		data = Database.executeSelectQuery("SELECT * FROM timeslot");
		Database.printData(data);
		System.out.println();
	}
}
