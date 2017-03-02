
public class Main {
	
	public static void main(String args[]){
		
		/* DUmmy Company Scedule, 2 10 hour shifts a day, 7 day of the week */
		Schedule compSchedule = new Schedule();
		for(int i = 0; i < 7; i++){
			compSchedule.addTimeBlock(i, 0, 10, "worker");
			compSchedule.addTimeBlock(i, 1, 11, "worker");
			compSchedule.addTimeBlock(i, 10, 20, "worker");
		}
		
		
		//Fill Company Pool with Shifts
		compSchedule.fillCompanyPool();

		
		
		/* Intialize Dummy Employee workers*/
		Employee[]	staff = new Employee[10];
		for(int i = 0; i < staff.length; ++i){
			// Assign open availability all day
			Schedule empSchedule = new Schedule();
			for(int j = 0; j < 7; j++){
				empSchedule.addTimeBlock(j, 0, 20, "worker");
			}
			
			//Fill employee shiftpool
			empSchedule.fillBlockPool(compSchedule);
			
			//Set prefrences - iteration 1 = RANDOMIZE
			empSchedule.setSchedulePrefrences();
			
			//Set Name
			String name = "worker" + i ;
			staff[i] = new Employee(i, name, 40, empSchedule);
		}
		
		Schedule NewSchedule = compSchedule.generateNewSchedule(compSchedule, staff);
		
		NewSchedule.printSchedule();
		
		
	}
	

}
