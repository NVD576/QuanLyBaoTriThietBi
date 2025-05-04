<%-- 
    Document   : index
    Created on : Apr 30, 2025, 10:37:20 PM
    Author     : ADMIN
--%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Trang chủ</title>
    </head>
    <body>
        <h1>Hello World!</h1>
        <ul>
            <c:forEach items="${equipment}" var="p">
                <li>${p.id} - ${p.name} - ${p.code}- dsads</li>
            </c:forEach>
        </ul>
    </body>
</html>
