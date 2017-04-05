<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>The Greatest Schedule Ever Scheduled</title>
<link rel="stylesheet" type="text/css" href="style.css">
<%@ page import="model.Employee" %>
</head>
<body>


<div class="navbar">
	<h1>The Greatest Schedule Ever Scheduled</h1>
	<a href="home.jsp">Home</a>
	<a href="updateUnavailability.jsp">Update Un-availabilities</a>
	<c:if test="Employee.getLoggedIn().getIsManager()"> <a href="updateShifts.jsp">Update Shifts</a></c:if>
	<a href="loginPage.jsp">Logout</a>
</div>
<br/>
<div class="center">
	
	<% String loggedIn = "Employee Logged in: " + Employee.getLoggedIn().getFullName(); %>
	<%= loggedIn %>
	<h1>Current Shifts: </h1>
	<%= Employee.getLoggedIn().getSchedule() %>
	

</div>


</body>
</html>