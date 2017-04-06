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
<%= Navbar.navbar %>
<br/>
<div class="center">
	
	
	<form method="post" action="generateSchedule" id="generateScheduleForm">
		<input type="submit" name="Generate New Schedule"></input>
	</form>
	<%= Employee.getLoggedIn().getBusiness().getMasterSchedule().toString() %>

</div>
</body>
</html>