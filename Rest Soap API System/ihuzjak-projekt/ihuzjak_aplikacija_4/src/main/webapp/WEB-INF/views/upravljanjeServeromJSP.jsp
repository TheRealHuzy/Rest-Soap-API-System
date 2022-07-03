<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Huzjak - Aplikacija 4 - upravljanje serverom</title>
</head>
<body>
	<h1>Upravljanje serverom</h1>
	<a href="${pageContext.servletContext.contextPath}/mvc/korisnici/pocetna">Početna</a>
	<br>
	<c:if test = "${requestScope.porukaSer == null}">
		<label>Status servera udaljenosti >> ${requestScope.status}</label>
		<form method="post">
			<select name="slcKomande" id="slcKomande">
				<c:forEach var="k" items="${requestScope.komande}">
					<option>${k.getValue()}</option>
				</c:forEach>
			</select>
			<input type="submit" value="Pošalji komandu">
		</form>
		<c:if test = "${requestScope.odgovorServera != null}">
			<label>Odgovor servera udaljenosti >> ${requestScope.odgovorServera}</label>
		</c:if>
	</c:if>
	<c:if test = "${requestScope.porukaSer != null}">
		<label>Info >> ${requestScope.porukaSer}</label>
	</c:if>
</body>
</html>