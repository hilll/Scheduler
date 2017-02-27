
public class Employee {
	
	int empID;
	String empName;
	int prefHours;
	Day[] empAvailability;
	
	public Employee(int id, String name, int hours, Day[] avail){
		this.empID = id;
		this.empName = name;
		this.prefHours=hours;
		this.empAvailability = avail;
	}

}
