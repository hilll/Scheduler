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
<%@ page import="java.util.ArrayList" %>
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
<div>
	
	<% String loggedIn = "Employee Logged in: " + Employee.getLoggedIn().getFullName(); %>
	<%= loggedIn %>

	
	<h1>Current Shifts: </h1>
<%ArrayList<String>[] dailyShifts = Employee.getLoggedIn().getScheduledShiftsAsBinaryStrings();%>
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
	
	<pre>
<%= Employee.getLoggedIn().getSchedule() %>
	</pre>

</div>


</body>
</html>