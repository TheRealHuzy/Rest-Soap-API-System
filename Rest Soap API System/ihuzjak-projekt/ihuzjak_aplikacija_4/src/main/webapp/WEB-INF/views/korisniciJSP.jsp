<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Huzjak - Aplikacija 4 - pregled svih korisnika</title>
</head>
<body>
	<h1>Pregled svih korisnika</h1>
	<a href="${pageContext.servletContext.contextPath}/mvc/korisnici/pocetna">Početna</a>
	<br>
	<c:if test = "${requestScope.porukaKor == null && requestScope.brisanjeTokena == false}">
		<table border="1">
			<tr>
				<th>Korisnikčko ime</th>
				<th>Ime</th>
				<th>Prezime</th>
				<th>Lozinka</th>
				<th>Email</th>
				<c:if test = "${requestScope.admin == true}">
					<th>Invalidiraj</th>		
				</c:if>
			</tr>
			<c:forEach var="k" items="${requestScope.korisnici}">
				<tr>
					<td>${k.korisnik}</td>
					<td>${k.ime}</td>
					<td>${k.prezime}</td>
					<td>${k.lozinka}</td>
					<td>${k.email}</td>
					<c:if test = "${requestScope.admin == true}">
						<td>
							<form method="post">
					        	<input type="hidden" id="korisnikZaBrisanje" name="korisnikZaBrisanje" value="${k.korisnik}">
					        	<input type="hidden" id="operacija" name="operacija" value="0">
					            <input type="submit" value="Invalidiraj tokene">
		        			</form>
		        		</td>	
					</c:if>
				</tr>
			</c:forEach>
		</table>
	</c:if>	
	<c:if test = "${requestScope.porukaKor == null && requestScope.brisanjeTokena == false}">
		<form method="post">
	       	<input type="hidden" id="korisnikZaBrisanje" name="korisnikZaBrisanje" value="${requestScope.token}">
	       	<input type="hidden" id="operacija" name="operacija" value="1">
	        <input type="submit" value="Obriši moj trenutni token">
	    </form>
    </c:if>
    <c:if test = "${requestScope.porukaKor != null}">
		<label>Info >> ${requestScope.porukaKor}</label>
    </c:if> 
</body>
</html>