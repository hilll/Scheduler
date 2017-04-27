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
<title>Update Shifts</title>
<link rel="stylesheet" type="text/css" href="style.css">

</head>
<body>
<%String navbar;
if(Employee.getLoggedIn().getIsManager()){ 
	navbar = Navbar.navbar + Navbar.managerNavbar + "</div>";
}else{
	navbar = Navbar.navbar + "</div>";
}%>

<%= navbar %>

<br/>
<br/>
<br/>
<br/>
<div class="main">
<h1> Current Shifts</h1>
<%ArrayList<String>[] dailyShifts = Employee.getLoggedIn().getBusiness().getMasterSchedule().getShiftsAsBinaryStrings();%>
<table style="width:100%">
  <tr>
  	<th>Time</th>
  	<th colspan ="<%= dailyShifts[0].size() %>">Sunday </th>
    <th colspan= "<%= dailyShifts[1].size() %>">Monday</th> 
    <th colspan= "<%= dailyShifts[2].size() %>">Tuesday</th>
    <th colspan= "<%= dailyShifts[3].size() %>">Wednesday</th>
    <th colspan= "<%= dailyShifts[4].size() %>">Thursday</th>
    <th colspan= "<%= dailyShifts[5].size() %>">Friday</th>
    <th colspan= "<%= dailyShifts[6].size() %>">Saturday</th>
    
  </tr>


<% for(int i = 0; i < 96; i++){ 
	String time; 
	int hour = i / 4 ;
	int minutes = i % 4 * 15; 
	String min;
	if (minutes == 0){
		 min = "00";
	}else{
		 min = minutes + "";
	}
	time = String.format( "%02d:%s", hour, min);%>
	<%= "<tr><td>" + time + "</td>" %>
	
	
	<%for(int j = 0; j < 7; j++){
		if(dailyShifts[j].size()==0){
			String shift = "noshift"; %>
			<%= "<td class=\"" + shift + "\">" %>
			</td>
		<% continue;
		}
		for(int k = 0; k < dailyShifts[j].size(); k++){
			String shift;
			if(dailyShifts[j].get(k).charAt(i)=='1'){
				shift = "shift";
			}else{
				shift = "noshift";
			}
		
	
	%>
		<%= "<td class=\"" + shift + "\">" %>
		</td>

	<% } %>
	
<% }%>
</tr>
<% }%>
</table>

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