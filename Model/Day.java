
public enum Day {
	
	SUNDAY(0,24), MONDAY(0,24), TUESDAY(0,24), WEDNESDAY(0,24), 
		THURSDAY(0,24), FRIDAY(0,24), SATURDAY(0,24);
	
	public int beginHour;
	public int endHour;
	
	Day(int begin, int end){
		this.beginHour = begin;
		this.endHour = end;
	}

}
