
public class Schedule {
	
	TimeSlot[] Sunday;
	TimeSlot[] Monday;
	TimeSlot[] Tuesday;
	TimeSlot[] Wednesday;
	TimeSlot[] Thursday;
	TimeSlot[] Friday;
	TimeSlot[] Saturday;
	
	public Schedule(TimeSlot[] sun, TimeSlot[] mon, TimeSlot[] tues, 
			TimeSlot[] wed, TimeSlot[] thurs, TimeSlot[] fri, TimeSlot[] sat){
		
		this.Sunday = sun;
		this.Monday = mon;
		this.Tuesday = tues;
		this.Wednesday = wed;
		this.Thursday = thurs;
		this.Friday = fri;
		this.Saturday = sat;
	}
	

}
