package controller;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.*;
import javax.servlet.http.*;


import model.*;

public class LoginController extends HttpServlet{
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
			String username = (String) request.getParameter("username");
			String password = (String) request.getParameter("password");
			
			PrintWriter out = response.getWriter();
			out.println("Got username: " + username);
			out.println("Got password: " + password);

	}

}
