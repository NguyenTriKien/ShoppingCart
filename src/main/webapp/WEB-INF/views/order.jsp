<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ page isELIgnored="false"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<c:set var="contextPath" value="${pageContext.request.contextPath}"/>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>Insert title here</title>
<style type="text/css">
  <%@include file="css/styles.css" %> 
</style>
</head>
<body>
	<jsp:include page="_header.jsp" />	
	<jsp:include page="_menu.jsp" />
	
	<fmt:setLocale value="en_US" scope="session" />
	
	<div class="page-title">Order Info</div>
	
	<div class="customer-info-container">
		<h3>Customer Information</h3>
		<ul>
			<li>Name: ${orderInfo.customerName }</li>
			<li>Email: ${orderInfo.customerEmail }</li>
			<li>Phone: ${orderInfo.customerPhone}</li>
			<li>Address: ${orderInfo.customerAddress}</li>
		</ul>
		
		<h3>Order Summary: </h3>
		<ul>
			<li>Total:
				<span class="total">
					<fmt:formatNumber value="${orderInfo.amount }" type="currency"></fmt:formatNumber>
				</span>
			</li>
		</ul>
	</div>
	
	<br/>
	
	<table border="1" style="width: 100%">
		<tr>
			<th>Product Code: </th>
			<th>Product Name: </th>
			<th>Quantity: </th>
			<th>Price: </th>
			<th>Total: </th>
		</tr>
		<c:forEach items="${orderInfo.orderDetailInfos }" var="orderDetailInfos">
			<tr>
				<td>${orderDetailInfos.productCode }</td>
				<td>${orderDetailInfos.productName }</td>
				<td>${orderDetailInfos.quantity }</td>
				<td>
					<fmt:formatNumber value="${orderDetailInfos.price }" type="currency"></fmt:formatNumber>
				</td>
				<td>
					<fmt:formatNumber value="${orderDetailInfos.amount }" type="currency"></fmt:formatNumber>
				</td>
			</tr>
		</c:forEach>
	</table>
	
	<c:if test="${paginationOrderInfos.totalPages > 1 }">
		<div class="page-navigator">
			
			<c:forEach items="${paginationOrderInfos.navigationPages }" var="page">
				<c:if test="${page != -1 }">
					<a href="orderList?page=${page }" class="nav-item">${page }</a>
				</c:if>
				<c:if test="${page == -1 }">
					<span class="nav-item">...</span>
				</c:if>
			</c:forEach>
		</div>
	</c:if>
	
	<jsp:include page="_footer.jsp" />
</body>
</html>