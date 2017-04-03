<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    <%@ page import="model.*" %>
    <%@ page import="java.util.HashMap"%>
    <%@ page import="java.util.Iterator" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Update Availability</title>
<link rel="stylesheet" type="text/css" href="style.css">

</head>
<body>
<div class="navbar">
  <a href="home.jsp">Generate Schedule</a>
  <a href="updateAvailability.jsp">Update Availability</a>
</div>
<br/>
<div class="main">
<h1>Create a new Availability</h1>

<form method="post" action="createAvailability" id="createAvailabilityForm">
<label>Select a Day</label>
	<select name="daySelect" form="createAvailabilityForm">
		<option value = "0">Sunday</option>
		<option value = "1">Monday</option>
		<option value = "2">Tuesday</option>
		<option value = "3">Wednesday</option>
		<option value = "4">Thursday</option>
		<option value = "5">Friday</option>
		<option value = "6">Saturday</option>
	</select>
	<br>
<label>Start time</label><input type="number" min = "0"  max = "23" name = "start"><br>
<label>End Time</label><input type="number" min = "0"  max = "23" name = "end"><br>
<label>Enter the employee type</label>
	<select name="employeeTypeSelect" form="createAvailabilityForm">
		<option value = "false">Employee</option>
		<option value = "true">Manager</option>
	</select><br>

<input type="submit" value ="submit" name = "Create Shift">

<h1> Current Availabilities </h1>
<% HashMap<String, TimeSlot> unavailabilities = Employee.getLoggedIn().getAvailability().getUnavailabilitySlots(); 
	Iterator it = unavailabilities.entrySet().iterator();
	while (it.hasNext()) {
		HashMap.Entry pair = (HashMap.Entry)it.next();
	
	%>
	<% pair.getKey();%>
	<% } %>
</form>
</div>

</body>
</html>