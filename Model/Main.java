
public class Main {
	
	public static void main(String args[]){
		
		/* Intialize Dummy Employees*/
		Employee[]	staff = new Employee[10];
		for(int i = 0; i < staff.length; ++i){
			String name = "Staff(" + i + ")";
			staff[i] = new Employee(i, name, 40);
		}
		
		
		
	}
	

}
