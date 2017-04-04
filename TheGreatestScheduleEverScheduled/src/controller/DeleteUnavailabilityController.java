package controller;
	import java.io.IOException;
	import java.io.PrintWriter;

	import javax.servlet.*;
	import javax.servlet.http.*;
	import model.*;
	
public class DeleteUnavailabilityController extends HttpServlet{

		
		public void doPost(HttpServletRequest request, HttpServletResponse response)
				throws ServletException, IOException {
				String deleteSlot = request.getParameter("deleteSlot");
				
				Employee.getLoggedIn().getAvailability().getUnavailabilitySlots().remove(deleteSlot);
				Employee.getLoggedIn().getAvailability().save(Employee.getLoggedIn().getID());
				response.sendRedirect("updateUnavailability.jsp");


		}

}
