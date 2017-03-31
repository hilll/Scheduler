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
	<h1>The Greatest Schedule Ever Scheduled <h1><br>
	<a href="employeeHome.jsp">Home</a>
	<a href="updateAvailability.jsp">Change Availability</a>
</div>
<br/>
<div class="main">
	<% int loggedIn = Employee.getLoggedIn(); %>
	<%= "loggedIn employee ID: " + Employee.getLoggedIn() %>

</div>


</body>
</html>