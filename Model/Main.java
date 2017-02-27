
public class Main {
	
	public static void main(String args[]){
		
		/* Company Scedule */
		Schedule compSchedule = new Schedule();
		for(int i = 0; i < 7; i++){
			compSchedule.AddTimeSlot(new TimeSlot(i, 0, 24));
		}
		
		
		/* Intialize Dummy Employees*/
		Employee[]	staff = new Employee[10];
		for(int i = 0; i < staff.length; ++i){
			// Assign open availability all day
			Schedule empSchedule = new Schedule();
			for(int j = 0; j < 7; j++){
				empSchedule.AddTimeSlot(new TimeSlot(j, 0, 24));
			}
			String name = "Staff(" + i + ")";
			staff[i] = new Employee(i, name, 40, empSchedule);
		}
		
		
		
	}
	

}
