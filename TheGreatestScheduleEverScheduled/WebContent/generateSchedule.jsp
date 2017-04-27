<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<%@ page import="model.Employee" %>
<%@ page import="controller.Navbar" %>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" type="text/css" href="style.css">

<title>Generate Schedule</title>
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
<div class="center">
	
	
	<form method="post" action="generateSchedule" id="generateScheduleForm">
		<label>Generate a New Schedule</label>
		<input type="submit" value ="submit" name="Generate New Schedule">
	</form>
	<%= Employee.getLoggedIn().getBusiness().getMasterSchedule().toString() %>

</div>
</body>
</html>