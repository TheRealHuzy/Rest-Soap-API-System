<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Huzjak - Aplikacija 4 - registracija</title>
</head>
<body>
	<h1>Registracija</h1>
	<a href="${pageContext.servletContext.contextPath}/mvc/korisnici/pocetna">Početna</a>
	<br>
	<form method="post">
		<input type="text" id="korisnik" name="korisnik" value="${requestScope.korisnikReg}"><br><br>
		<input type="text" id="lozinka" name="lozinka" value="${requestScope.lozinkaReg}"><br><br>
		<input type="text" id="ime" name="ime" placeholder="Ime..."><br><br>
		<input type="text" id="prezime" name="prezime" placeholder="Prezime..."><br><br>
		<input type="text" id="email" name="email" placeholder="Email..."><br><br>
		<input type="submit" value="Registriraj me">
	</form>
	<c:if test = "${requestScope.porukaReg != null}">
		<label>Info >> ${requestScope.porukaReg}</label>		
	</c:if>
</body>
</html>