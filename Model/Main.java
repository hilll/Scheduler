
public class Main {
	
	public static void main(String args[]){
		
		/* Intialize Dummy Employees*/
		Employee[]	staff = new Employee[10];
		for(int i = 0; i < staff.length; ++i){
			Day[] availability = new Day[]{Day.SUNDAY, Day.MONDAY, Day.TUESDAY, Day.WEDNESDAY, Day.THURSDAY, Day.FRIDAY, Day.SATURDAY};
			String name = "Staff(" + i + ")";
			staff[i] = new Employee(i, name, 40, availability);
		}
		
		
		
	}
	

}
