
public class Main {
	
	public static void main(String args[]){
		
		/* DUmmy Company Scedule, 2 10 hour shifts a day, every day of the week */
		Schedule compSchedule = new Schedule();
		for(int i = 0; i < 7; i++){
			compSchedule.AddTimeBlock(new TimeSlot(i, 0, 10));
			compSchedule.AddTimeBlock(new TimeSlot(i, 10, 20));
		}
		
		
		/* Intialize Dummy Employees*/
		Employee[]	staff = new Employee[10];
		for(int i = 0; i < staff.length; ++i){
			// Assign open availability all day
			Schedule empSchedule = new Schedule();
			for(int j = 0; j < 7; j++){
				empSchedule.AddTimeBlock(new TimeSlot(j, 0, 20));
			}
			String name = "Staff(" + i + ")";
			staff[i] = new Employee(i, name, 40, empSchedule);
		}
		
		
		
	}
	

}
