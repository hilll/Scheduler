package controller;

public class Navbar {
	public static final String navbar = "<div class=\"navbar\"> "
			+ "<h1>The Greatest Schedule Ever Scheduled</h1>"
			+ "<a href=\"home.jsp\">Home</a>"
			+ "<a href=\"updateUnavailability.jsp\">Update Un-availabilities</a>"
			+ "<c:if test=\"Employee.getLoggedIn().getIsManager()\">"
			+ " <a href=\"updateShifts.jsp\">Update Shifts</a>"
			+ " <a href=\"generateSchedule.jsp\">Master Schedule</c:if>"
			+ "<a href=\"loginPage.jsp\">Logout</a></div>";
}
