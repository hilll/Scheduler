package controller;
	import java.io.IOException;
	import java.io.PrintWriter;

	import javax.servlet.*;
	import javax.servlet.http.*;
	import model.*;
	
public class AddUnavailabilityController extends HttpServlet{

		
		public void doPost(HttpServletRequest request, HttpServletResponse response)
				throws ServletException, IOException {
				int day = Integer.parseInt(request.getParameter("daySelect"));
				int start = Integer.parseInt(request.getParameter("start"));
				int end = Integer.parseInt(request.getParameter("end"));
				//boolean isManager = request.getParameter("employeeTypeSelect").equals("true");
				
				Employee.getLoggedIn().getAvailability().updateAvailabilityStrings(day, start, end, '1');
				Employee.getLoggedIn().save();
				response.sendRedirect("updateUnavailability.jsp");


		}

}
