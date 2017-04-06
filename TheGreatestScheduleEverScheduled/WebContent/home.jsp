<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>The Greatest Schedule Ever Scheduled</title>
<link rel="stylesheet" type="text/css" href="style.css">
<%@ page import="model.Employee" %>
<%@ page import="controller.Navbar" %>
</head>
<body>
<%= Navbar.navbar %>

<br/>
<div class="center">
	
	<% String loggedIn = "Employee Logged in: " + Employee.getLoggedIn().getFullName(); %>
	<%= loggedIn %>
	<h1>Current Shifts: </h1>
	<%= Employee.getLoggedIn().getSchedule() %>
	

</div>


</body>
</html>