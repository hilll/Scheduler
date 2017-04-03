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
	<div class="login">
		<c:if test="!LoginController.getLoginFailed().equals(\"\")"> <%= LoginController.getLoginFailed() %> </c:if>
		<form method="post" action="login">
			<input type="text" name="username"><br>
			<input type="password" name = "password"><br>
			<input type="submit" value ="submit" href="employeeHome.jsp">
		</form>
	</div>
</body>