package controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import model.Employee;

public class GenerateScheduleController extends HttpServlet{

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
			Employee.getLoggedIn().getBusiness().generateNewSchedule();
			response.sendRedirect("generateSchedule.jsp");

	}
	
}
