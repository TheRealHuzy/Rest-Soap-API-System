<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Huzjak - Aplikacija 4</title>
</head>
<body>
	<h1>Početna</h1>

	<ul>
		<li><a href="${pageContext.servletContext.contextPath}/mvc/registracija">Registracija</a></li>		
		<li><a href="${pageContext.servletContext.contextPath}/mvc/prijava">Prijava</a></li>
		<li><a href="${pageContext.servletContext.contextPath}/mvc/korisnici">Pregled korisnika</a></li>
		<li><a href="${pageContext.servletContext.contextPath}/mvc/server">Upravljanje serverom udaljenosti</a></li>
	</ul>
	<c:if test = "${requestScope.odjava == false}">
		<form method="post">
			<input type="submit" value="Odjavi se">
		</form>
	</c:if>
</body>
</html>