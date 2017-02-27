
public class Main {
	
	public static void main(String args[]){
		
		/* Intialize Dummy Employees*/
		Employee[]	staff = new Employee[10];
		for(int i = 0; i < staff.length; ++i){
			// Assign open availability all day
			TimeSlot sunday = new TimeSlot(Day.SUNDAY, 0, 24);
			TimeSlot monday = new TimeSlot(Day.MONDAY, 0, 24);
			TimeSlot tuesday = new TimeSlot(Day.TUESDAY, 0, 24);
			TimeSlot wednesday = new TimeSlot(Day.WEDNESDAY, 0, 24);
			TimeSlot thursday = new TimeSlot(Day.THURSDAY, 0, 24);
			TimeSlot friday = new TimeSlot(Day.FRIDAY, 0, 24);
			TimeSlot saturday = new TimeSlot(Day.SATURDAY, 0, 24);
			TimeSlot[] availability = new TimeSlot[]{sunday, monday, tuesday, wednesday, thursday, friday, saturday};
			String name = "Staff(" + i + ")";
			staff[i] = new Employee(i, name, 40, availability);
		}
		
		
		
	}
	

}
