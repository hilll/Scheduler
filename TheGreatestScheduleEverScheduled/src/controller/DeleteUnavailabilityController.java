package controller;
	import java.io.IOException;
	import java.io.PrintWriter;

	import javax.servlet.*;
	import javax.servlet.http.*;
	import model.*;
	
public class DeleteUnavailabilityController extends HttpServlet{

		
		public void doPost(HttpServletRequest request, HttpServletResponse response)
				throws ServletException, IOException {
				String[] deleteSlots = request.getParameterValues("deleteSlot");
				for( String s : deleteSlots){
					Employee.getLoggedIn().getAvailability().removeUnavailability(s);
				}
				Employee.getLoggedIn().getAvailability().save(Employee.getLoggedIn().getID());
				response.sendRedirect("updateUnavailability.jsp");


		}

}
