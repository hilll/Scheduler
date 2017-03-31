package controller;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.*;
import javax.servlet.http.*;


import model.*;

public class LoginController extends HttpServlet{
	
	String loginFailed = "";
	
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
			String username = (String) request.getParameter("username");
			String password = (String) request.getParameter("password");
			
			int id = Employee.getIDForLogin(username, password);
			PrintWriter out = response.getWriter();
			
			if(id < 0 ){
				out.println("Login Failed");
				loginFailed = "Login Failed";
			}else{
				loginFailed = "";
				out.printf(" got employee ID:%d\n", id);
			}
			out.println("Got username: " + username);
			out.println("Got password: " + password);

	}

}
