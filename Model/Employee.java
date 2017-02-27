
public class Employee {
	
	int empID;
	String empName;
	int prefHours;
	TimeSlot[] empAvailability;
	
	public Employee(int id, String name, int hours, TimeSlot[] avail){
		this.empID = id;
		this.empName = name;
		this.prefHours=hours;
		this.empAvailability = avail;
	}

}
