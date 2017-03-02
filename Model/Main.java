
public class Main {
	
	public static void main(String args[]){
		
		/* DUmmy Company Scedule, 2 10 hour shifts a day, 7 day of the week */
		Schedule compSchedule = new Schedule();
		for(int i = 0; i < 7; i++){
			compSchedule.addTimeBlock(i, 0, 10, "worker");
			compSchedule.addTimeBlock(i, 10, 20, "worker");
			compSchedule.addTimeBlock(i, 0, 20, "manager");
		}
		
		
		//Fill Company Pool with Shifts
		compSchedule.fillCompanyPool();

		
		
		/* Intialize Dummy Employee workers*/
		Employee[]	staff = new Employee[12];
		int i;
		for( i = 0; i < 10; ++i){
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
		/* Intialize Dummy Employee workers*/
		for( int k = 0; i < 12; ++i){
			// Assign open availability all day
			Schedule empSchedule = new Schedule();
			for(int j = 0; j < 7; j++){
				empSchedule.addTimeBlock(j, 0, 20, "manager");
			}
			
			//Fill employee shiftpool
			empSchedule.fillBlockPool(compSchedule);
			
			//Set prefrences - iteration 1 = RANDOMIZE
			empSchedule.setSchedulePrefrences();
			
			//Set Name
			String name = "manager" + k ;
			k++;
			staff[i] = new Employee(i, name, 40, empSchedule);
		}
		
		
		Schedule NewSchedule = compSchedule.generateNewSchedule(compSchedule, staff);
		
		NewSchedule.printSchedule();
		
		
	}
	

}
