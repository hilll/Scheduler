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
	<h1>The Greatest Schedule Ever Scheduled</h1>
	<a href="home.jsp">Home</a>
	<a href="addAvailability.jsp">Add Availability</a>
	<a href="loginPage.jsp">Logout</a>
</div>
<br/>
<div class="main">
<h1>Delete an Unavailability</h1>


<form method="post" action="changeAvailability" id="deleteAvailabilityForm">
<h1> Current Unavailabilities </h1>
<% HashMap<String, TimeSlot> unavailabilities = Employee.getLoggedIn().getAvailability().getUnavailabilitySlots(); 
	Iterator it = unavailabilities.entrySet().iterator();
	int i = 0;
	while (it.hasNext()) {
		HashMap.Entry pair = (HashMap.Entry)it.next();
		i++;
	
	%>
	<input type="radio" name="deleteSlot" value="<%=pair.getKey() %>">
	<%= i + "   " + pair.getKey() %>
	<br>
	<% } %>
	<br><br>
	<input type="submit" value="Delete Selected Shift"></input> 
</form>

</body>
</html>