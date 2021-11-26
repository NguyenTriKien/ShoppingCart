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
<title>Order List</title>
<style type="text/css">
  <%@include file="css/styles.css" %> 
</style>
</head>
<body>
    <jsp:include page="_header.jsp" />
    <jsp:include page="_menu.jsp" />
    
    <fmt:setLocale value="en_US" scope="session"/>
    
    <div class="page-title">Order List</div>
    
    <div>Total Order Count: ${paginationOrderInfos.totalRecords}</div>
    
    <table border="1" style="width: 100%">
        <tr>
           <th>Order Num</th>
           <th>Order Date</th>
           <th>Customer Name</th>
           <th>Customer Address</th>
           <th>Customer Email</th>
           <th>Amount</th>
           <th>View</th>
        </tr>
        <c:forEach items="${paginationOrderInfos.list}" var = "orderInfo">
           <tr>
              <td>${orderInfo.orderNum}</td>
              <td>
                 <fmt:formatDate value="${orderInfo.orderDate}" pattern="dd-MM-yyyy HH:mm" />
              </td> 
              <td>${orderInfo.customerName}</td>
              <td>${orderInfo.customerAddress}</td>
              <td>${orderInfo.customerEmail}</td>
              <td style="color: red;">
                 <fmt:formatNumber value="${orderInfo.amount}" type="currency" />
              </td>
              <td>
                 <a href="${contextPath}/order?orderId=${orderInfo.id}">View</a>
              </td>
           </tr>
         </c:forEach>
       </table>
       <c:if test="${paginationOrderInfos.totalPages > 1 }">
           <div class="page-navigatior">
              
              <c:forEach items="${paginationOrderInfos.navigationPages}" var="page">
                 <c:if test="${page != -1 }">
                    <a href="orderList?page=${page}" class="nav-item">${page}</a>
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