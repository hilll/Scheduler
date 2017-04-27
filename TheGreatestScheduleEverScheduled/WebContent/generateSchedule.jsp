<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<%@ page import="model.Employee" %>
<%@ page import="controller.Navbar" %>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" type="text/css" href="style.css">

<title>Master Schedule</title>
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
<div>
	
	
	<form method="post" action="generateSchedule" id="generateScheduleForm">
	<pre>
<label>Generate a New Schedule</label>
<input type="submit" value ="submit" name="Generate New Schedule">
	</pre>
	</form>
	<pre>
<%= Employee.getLoggedIn().getBusiness().getMasterSchedule().toString() %>
	</pre>
</div>
</body>
</html>