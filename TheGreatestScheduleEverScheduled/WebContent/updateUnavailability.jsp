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
<title>Update Availability</title>
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
<br/>
<br/>
<div class="main">

<h2>Current Availabilities</h2>
<table style="width:100%">
  <tr>
  	<th>Time</th>
    <th>Sunday</th>
    <th>Monday</th>
    <th>Tuesday</th>
    <th>Wednesday</th>
    <th>Thursday</th>
    <th>Friday</th>
    <th>Saturday</th>
    
   
  </tr>

<%String[] dailyAvailability = Employee.getLoggedIn().getAvailability().getAvailabilityStrings();

for(int i = 0; i < dailyAvailability[0].length(); i++){ %>
	<%String time; 
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
	
	
	<%for(int j = 0; j < dailyAvailability.length; j++){
		String available;
		if(dailyAvailability[j].charAt(i)=='0'){
			available = "unavailable";
		}else{
			available = "available";
		} %>
		<%= "<td class=\"" + available + "\">" %>
		</td>

	<% } %>
	</tr>
<% } %>
</table>

<h1>Create a New Unavailability</h1>

<form method="post" action=addUnavailability id="availabilityForm">
<label>Select a Day</label>
	<select name="daySelect" form="availabilityForm">
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
<select name = "start" form="availabilityForm"><br>
	<% for(int j = 0; j < 96; j++){ %>
	<option value = "<%= j %>"><%= TimeSlot.intToString(j) %></option>
	<%} %>
	</select>
<label>End Time</label>
<select name = "end" form="availabilityForm">
	<% for(int j = 0; j < 96; j++){ %>
	<option value = "<%= j %>"><%= TimeSlot.intToString(j) %></option>
	<%} %>
</select>
<br>

<input type="submit" value ="submit" name = "Create Shift">
</form>

<h1> Delete Unavailability </h1>
<form method="post" action="deleteUnavailability" id="deleteAvailabilityForm">
<% ArrayList<String> unavailabilities = Employee.getLoggedIn().getAvailability().getUnavailabilitySlots(); 
	for (int i = 0; i < unavailabilities.size(); i++) {
		String u = unavailabilities.get(i);	
	%>
	<input type="checkbox" name="deleteSlot" value="<%=u %>">
	<%=  u %>
	<br>
	<% } %>
	<br><br>
	<input type="submit" value="Delete Selected Unavailability"></input> 
</form>
</div>

</body>
</html>