package controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import model.Employee;

public class DeleteShiftController  extends HttpServlet{
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
			String[] deleteSlots = request.getParameterValues("deleteSlot");
			for(String s : deleteSlots){
				//TODO: delete shift
				Employee.getLoggedIn().getBusiness().removeShift(s);
			}
			//TODO: save
			response.sendRedirect("updateShifts.jsp");
	}

}
