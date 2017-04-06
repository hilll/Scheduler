package controller;
	import java.io.IOException;
	import java.io.PrintWriter;

import javax.servlet.*;
	import javax.servlet.http.*;
	import model.*;
	
public class AddShiftController  extends HttpServlet{
		
		public void doPost(HttpServletRequest request, HttpServletResponse response)
				throws ServletException, IOException {
				int day = Integer.parseInt(request.getParameter("daySelect"));
				int start = Integer.parseInt(request.getParameter("start"));
				int end = Integer.parseInt(request.getParameter("end"));
				boolean managerShift = request.getParameter("employeeTypeSelect").equals("true");
				
				//*********add the timeslot
				Employee.getLoggedIn().getBusiness().createShift(day, start, end, managerShift);
				response.sendRedirect("updateShifts.jsp");


		}

}
