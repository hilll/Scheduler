package test;

import model.Employee;

public class Driver {

	public static void main(String[] args) {
		//Employee emp = new Employee(-1, "Just", "Testing", "justtesting@idk.com", null, false);
		//if (emp.save()) {
		//	System.out.println("yay!");
		//} else {
		//	System.out.println("Returned false.");
		//}
		Employee.loadFromID(6);
	}
}
