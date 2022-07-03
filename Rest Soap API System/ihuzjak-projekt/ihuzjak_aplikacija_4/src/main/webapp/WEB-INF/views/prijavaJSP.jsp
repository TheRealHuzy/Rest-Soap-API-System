<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Huzjak - Aplikacija 4 - prijava</title>
</head>
<body>
	<h1>Prijava</h1>
	<a href="${pageContext.servletContext.contextPath}/mvc/korisnici/pocetna">Početna</a>
	<br>
	<form method="post">
		<input type="text" id="korisnik" name="korisnik" placeholder="Korisniko ime..."><br><br>
		<input type="text" id="lozinka" name="lozinka" placeholder="Lozinka..."><br><br>
		<input type="submit" value="Prijavi se">
	</form>
	<c:if test = "${requestScope.porukaPri != null}">
		<label>Info >> ${requestScope.porukaPri}</label>		
	</c:if>
</body>
</html>