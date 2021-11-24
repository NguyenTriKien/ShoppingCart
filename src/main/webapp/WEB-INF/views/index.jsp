<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ page isELIgnored="false"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib uri="http://www.springframework.org/security/tags"
	prefix="security"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<c:set var="contextPath" value="${pageContext.request.contextPath}" />
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>Index</title>
<style type="text/css"><%@include file="css/styles.css" %></style>
</head>
<body>

     <jsp:include page="_header.jsp"></jsp:include>
     <jsp:include page="_menu.jsp"></jsp:include>
     
     <div class="page-tittle">Shopping Cart</div>
     
     <div class="demo-container">
        <h3>Content</h3>
        
        <ul>
           <li>Buy online</li>
           <li>Admin page</li>
           <li>Reports</li>
        </ul>
     </div>
     <jsp:include page="_footer.jsp" />
</body>
</html>