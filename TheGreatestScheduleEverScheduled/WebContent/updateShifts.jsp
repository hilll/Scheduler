<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    <%@ page import="model.*" %>
    <%@ page import="java.util.HashMap"%>
    <%@ page import="java.util.Iterator" %>
    <%@ page import="java.util.ArrayList" %>
    <%@ page import="controller.Navbar" %>
    
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Delete Shift</title>
<link rel="stylesheet" type="text/css" href="style.css">

</head>
<body>
<%= Navbar.navbar %>

<br/>
<div class="main">
<h1>Create a New Shift</h1>

<form method="post" action="addShift" id="shiftForm">
<label>Select a Day</label>
	<select name="daySelect" form="shiftForm">
		<option value = "0">Sunday</option>
		<option value = "1">Monday</option>
		<option value = "2">Tuesday</option>
		<option value = "3">Wednesday</option>
		<option value = "4">Thursday</option>
		<option value = "5">Friday</option>
		<option value = "6">Saturday</option>
	</select>
	<br>
<label>Start time</label>
<select name = "start" form="shiftForm"><br>
	<% for(int j = 0; j < 96; j++){ %>
	<option value = "<%= j %>"><%= TimeSlot.intToString(j) %></option>
	<%} %>
	</select>
<label>End Time</label>
<select name = "end" form="shiftForm">
	<% for(int j = 0; j < 96; j++){ %>
	<option value = "<%= j %>"><%= TimeSlot.intToString(j) %></option>
	<%} %>
</select>
<br>
<label>Enter the employee type</label>
	<select name="employeeTypeSelect" form="shiftForm">
		<option value = "false">Employee</option>
		<option value = "true">Manager</option>
	</select><br>

<input type="submit" value ="submit" name = "Create Shift">
</form>

<h1> Delete Shift </h1>
<form method="post" action="deleteShift" id="deleteShiftForm">
<% ArrayList<String> shifts = Employee.getLoggedIn().getBusiness().getMasterSchedule().getAllShiftsAsStrings(); 
	for (int i = 0; i < shifts.size(); i++) {
		String s = shifts.get(i);	
	%>
	<input type="checkbox" name="deleteSlot" value="<%=s %>">
	<%= s %>
	<br>
	<% } %>
	<br><br>
	<input type="submit" value="Delete Selected Shift"></input> 
</form>

</div>
</body>
</html>