<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<title>The Greatest Schedule Ever Scheduled</title>
	<%@ page import="model.Employee" %>
	<%@ page import="controller.*" %>
<link rel="stylesheet" type="text/css" href="style.css">
</head>

<body>

	<% Employee.setLoggedIn(null); %>
	<div class="navbar">
		<h1 style="margin-left: auto; margin-right: auto;">The Greatest Schedule Ever Scheduled <h1><br>
	</div>
	<div class="center">
		<c:if test="!LoginController.getLoginFailed().equals(\"\")"> <p class="error"><%= LoginController.getLoginFailed() %> </p></c:if>
		<form method="post" action="login">
			<label>Username:</label><input type="text" name="username"><br>
			<label>Password:</label><input type="password" name = "password"><br>
			<input type="submit" value ="submit" href="employeeHome.jsp">
		</form>
	</div>
</body>