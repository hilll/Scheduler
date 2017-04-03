package model;

public enum Day {
	
	SUNDAY(0), MONDAY(1), TUESDAY(2), WEDNESDAY(3), 
		THURSDAY(4), FRIDAY(5), SATURDAY(6);
	
	public int day;
	
	Day(int day){
		this.day = day;
	}
	
	public static String get(int day) {
		if (day == 0)
			return "Sunday";
		else if (day == 1)
			return "Monday";
		else if (day == 2)
			return "Tuesday";
		else if (day == 3)
			return "Wednesday";
		else if (day == 4)
			return "Thursday";
		else if (day == 5)
			return "Friday";
		else if (day == 6)
			return "Saturday";
		else 
			return "Not a day.";
	}

}
