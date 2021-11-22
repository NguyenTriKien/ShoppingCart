<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ page isELIgnored="false"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<c:set var="contextPath" value="${pageContext.request.contextPath}" />
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>Shopping Cart Finalize</title>
<style type="text/css"><%@include file="css/styles.css" %></style>
</head>
<body>
   <jsp:include page="_header.jsp" />
   
   <jsp:include page="_menu.jsp" />
   
   <div class="page-title">Finalize</div>
   
   <div class="container">
       <h3>Thank you for Order</h3>
       Your order number is: ${lastOrderedCartInfo.orderNum}
   </div>
   
   <jsp:include page="_footer.jsp" />

</body>
</html>