<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    <%@ page import="model.*" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Create Shift</title>
<link rel="stylesheet" type="text/css" href="style.css">

</head>
<body>
<div class="navbar">
  <a href="masterSchedule.jsp">Generate Schedule</a>
  <a href="createShift.jsp">Create Shift</a>
  <a href="deleteShift.jsp">Delete Shift</a>
  <a href="createSchedule.jsp">Create Schedule</a>
</div>
<br/>
<div class="main">
<h1>Create a Shift</h1>

<form method="post" action="createShift">
<label>Select a Day 0-6</label><input type="number" min = "0"  max = "6" name = "day"><br>
<label>Select a shift start time 0-23</label><input type="number" min = "0"  max = "23" name = "start"><br>
<label>Select a shift end time 0-23</label><input type="number" min = "0"  max = "23" name = "end"><br>
<label>Enter the employee type</label><input type="text" name = "type"><br>

<input type="submit" value ="submit" name = "Create Shift">

</form>
</div>

</body>
</html>