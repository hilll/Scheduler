package test;

import controller.Database;
import model.Availability;
import model.Business;
import model.Employee;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.sql.SQLException;

import org.junit.*;

public class DatabaseTest {
	

	@BeforeClass
	public static void setUp() throws Exception {
		Database.beginPersistentConnection();
	}

	@AfterClass
	public static void tearDown() throws Exception {
		Database.endPersistentConnection();
	}

	@Test
	public void testSelectQuery() {
		ArrayList<HashMap<String, String>> res = Database.executeSelectQuery("SELECT * FROM " + Employee.getTableName());
		assertTrue(res != null);
	}
	
	@Test
	public void testManipulateQuery() {
		String origFName = Database.executeSelectQuery("SELECT fname FROM " + Employee.getTableName() + " WHERE id=0").get(0).get("fname");
		boolean res = Database.executeManipulateDataQuery(String.format("UPDATE %s SET fname='%s' WHERE id=0", 
				Employee.getTableName(), origFName));
		assertTrue(res);
		assertFalse(Database.executeManipulateDataQuery("random words")); // prints error
		
	}
	 
	@Test
	public void testGetNextIdForTable() {
		assertTrue(Database.getNextIDForTable("thisDoesNotExist") == -1); // this will print an error, ignore it
		assertTrue(Database.getNextIDForTable("test_table") == 2);
		assertTrue(Database.getNextIDForTable("test_empty_table") == 0);
	}
	
	@Test
	public void testMasterSchedContainsIDPair() {
		assertTrue(Database.masterSchedContainsIDPair(0, 0));
		assertFalse(Database.masterSchedContainsIDPair(-2, 1));
	}
	
	@Test
	public void testTableContainsID() {
		assertTrue(Database.tableContainsID(Employee.getTableName(), 0));
		assertFalse(Database.tableContainsID(Availability.getTableName(), -1));
		assertFalse(Database.tableContainsID("not_a_table", 42));
	}
	
	/* THIS IS THE CODE ORIGINALLY IN Database.java, in case it is needed for reference
	public static void main(String args[]) {

		// TODO move all this to test file

		Business forTesting = Business.loadFromID(0);

		// a little test to see how things work
		ArrayList<HashMap<String, String>> data = executeSelectQuery("SELECT * FROM " + Employee.getTableName());
		System.out.println("Data in employee table: ");
		printData(data);
		System.out.println();

		ArrayList<HashMap<String, String>> availData = executeSelectQuery(
				"SELECT * FROM " + Availability.getTableName());
		System.out.println("Data in availability table: ");
		printData(availData);
		System.out.println();

		// to get the next ID to use for some table
		System.out.println("Next new employee will have ID of: " + getNextIDForTable(Employee.getTableName()));
		System.out.println();

		// adding a new employee to the DB
		// Employee e = new Employee(getNextIDForTable(Employee.getTableName()),
		// "Test", "Person", "email", null, false);
		Employee e = new Employee(-1, "Test", "Person", "email", null, false);
		e.setBusiness(forTesting);
		System.out.println("Inserting new Employee: " + e.getFullName());
		e.save();

		System.out.println("Data in employee table: ");
		data = executeSelectQuery("SELECT * FROM " + Employee.getTableName());
		printData(data);
		System.out.println();

		ArrayList<HashMap<String, String>> availDataAfterTestPerson = executeSelectQuery(
				"SELECT * FROM " + Availability.getTableName());
		System.out.println("Data in availability table: ");
		printData(availDataAfterTestPerson);
		System.out.println();

		// delete the employee, because we don't Test Person >:O!
		System.out.println("Deleting " + e.getFullName() + "...");
		e.delete();

		System.out.println("Data in employee table: ");
		data = executeSelectQuery("SELECT * FROM " + Employee.getTableName());
		printData(data);
		System.out.println();

		ArrayList<HashMap<String, String>> availDataAfterDeletingTestPerson = executeSelectQuery(
				"SELECT * FROM " + Availability.getTableName());
		System.out.println("Data in availability table: ");
		printData(availDataAfterDeletingTestPerson);
		System.out.println();

		// see the availability format for an employee - I only added data for
		// myself (emp_id=1) for now
		System.out.println("Availability test:");
		data = executeSelectQuery("SELECT * FROM availability WHERE emp_id = 1");
		printData(data);

		// UPDATE test - id 0 already exists in DB
		System.out.println("Update test:");
		System.out.println("BEFORE:");
		data = executeSelectQuery("SELECT * FROM employee WHERE id = 0");
		printData(data);
		Employee updateEmp = new Employee(0, "poop", "White", "pooping", null, false);
		updateEmp.save();
		System.out.println("AFTER:");
		data = executeSelectQuery("SELECT * FROM employee WHERE id = 0");
		printData(data);
		Employee resetEmp = new Employee(0, "Sarina", "White", "sarinarw@email.arizona.edu", null, false);
		resetEmp.save();
	}*/

}
